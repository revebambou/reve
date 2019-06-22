package cn.reve.service.impl;
import cn.reve.dao.*;
import cn.reve.pojo.goods.*;
import cn.reve.utils.IdWorker;
import cn.reve.utils.MapperUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import cn.reve.entity.PageResult;
import cn.reve.service.goods.SpuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service(timeout = 5000, interfaceClass = SpuService.class)
public class SpuServiceImpl implements SpuService {

    @Autowired
    private SpuMapper spuMapper;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private CategoryBrandMapper categoryBrandMapper;

    @Autowired
    private ExamineLogMapper examineLogMapper;

    @Autowired
    private ExamineLogRecordMapper examineLogRecordMapper;

    /**
     * 返回全部记录
     * @return
     */
    public List<Spu> findAll() {
        return spuMapper.selectAll();
    }

    /**
     * 分页查询
     * @param page 页码
     * @param size 每页记录数
     * @return 分页结果
     */
    public PageResult<Spu> findPage(int page, int size) {
        PageHelper.startPage(page,size);
        Page<Spu> spus = (Page<Spu>) spuMapper.selectAll();
        return new PageResult<Spu>(spus.getTotal(),spus.getResult());
    }

    /**
     * 条件查询
     * @param searchMap 查询条件
     * @return
     */
    public List<Spu> findList(Map<String, Object> searchMap) {
        Example example = createExample(searchMap);
        return spuMapper.selectByExample(example);
    }

    /**
     * 分页+条件查询
     * @param searchMap
     * @param page
     * @param size
     * @return
     */
    public PageResult<Spu> findPage(Map<String, Object> searchMap, int page, int size) {
        PageHelper.startPage(page,size);
        Example example = createExample(searchMap);
        Page<Spu> spus = (Page<Spu>) spuMapper.selectByExample(example);
        return new PageResult<Spu>(spus.getTotal(),spus.getResult());
    }

    /**
     * 根据Id查询
     * @param id
     * @return
     */
    public Spu findById(String id) {
        return spuMapper.selectByPrimaryKey(id);
    }

    /**
     * 新增
     * @param spu
     */
    public void add(Spu spu) {
        spuMapper.insert(spu);
    }

    /**
     * 修改
     * @param spu
     */
    public void update(Spu spu) {
        spuMapper.updateByPrimaryKeySelective(spu);
    }

    /**
     *  删除
     * @param id
     */
    public void delete(String id) {
        spuMapper.deleteByPrimaryKey(id);
    }

    @Override
    @Transactional
    public void saveGoods(Goods goods) {
        saveGoodsToDatabase(goods, false);
    }

    private void saveGoodsToDatabase(Goods goods, boolean update){

        Spu spu = goods.getSpu();
        Date date = new Date();
        Date updateDate = date;
        String spuId = spu.getId();
        if(!update){
            System.out.println("save");
            spuId = idWorker.nextId()+"";
            spu.setId(spuId);
            System.out.println(spu.getId() + " spuId");
            spuMapper.insertSelective(spu);
        }else{
            System.out.println("update");
            spuMapper.updateByPrimaryKeySelective(spu);
            updateDate = new Date();
//            spuId = spu.getId();
            Example example = MapperUtils.andEqualToWithSingleValue(Sku.class, "spuId", spuId);
            List<Sku> skuList = skuMapper.selectByExample(example);
            for (Sku sku : skuList) {
                skuMapper.deleteByPrimaryKey(sku);
            }
        }
        System.out.println("spuId:" + spuId);
        System.out.println("save skuList");
        String spuName = spu.getName();
        int categoryId = spu.getCategory3Id();
        int brandId = spu.getBrandId();
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        String categoryName = category.getName();

        //save in tb_category_brand
        CategoryBrand categoryBrand = new CategoryBrand();
        categoryBrand.setBrandId(brandId);
        categoryBrand.setCategoryId(categoryId);
        int count = categoryBrandMapper.selectCount(categoryBrand);
        if(count==0){
            categoryBrandMapper.insertSelective(categoryBrand);
        }

        //save sku lists
        List<Sku> skuList = goods.getSkuList();
        for (Sku sku : skuList) {
            String spec = sku.getSpec();
            //if there is only one sku in a spu, that is, the sku.spec is null, assign with "{}"
            String skuName = spuName;
            if(spec==null || "".equals(spec)){
                sku.setSpec("{}");
            }else {
                Map<String, String> specMap = JSON.parseObject(spec, Map.class);
                for (String value : specMap.values()) {
                    skuName += " " + value;
                }
            }
            sku.setId(idWorker.nextId()+"");
            sku.setSpuId(spuId);
            sku.setCategoryId(categoryId);
            sku.setCategoryName(categoryName);
            sku.setCreateTime(date);
            sku.setUpdateTime(updateDate);
            sku.setName(skuName);
            skuMapper.insertSelective(sku);
        }
    }

    @Override
    public Goods findGoodsBySpuId(String spuId) {
        Spu spu = spuMapper.selectByPrimaryKey(spuId);
        List<Sku> skuList = (List<Sku>)skuMapper.selectByPrimaryKey(spuId);
        Goods goods = new Goods();
        goods.setSkuList(skuList);
        goods.setSpu(spu);
        return goods;
    }

    @Override
    @Transactional
    public void updateGoods(Goods goods) {
        saveGoodsToDatabase(goods, true);
    }

    @Override
    @Transactional
    public void updateSpuViaExamine(String spuId, String radio, String memo) {
        Spu spu = new Spu();
        spu.setId(spuId);
        spu.setIsMarketable(radio);
        spu.setStatus("1");
        spuMapper.updateByPrimaryKeySelective(spu);
        //save the examine log

        Date date = new Date();
        ExamineLog examineLog = new ExamineLog();
        examineLog.setUpdateTime(date);
        int examineId = 0;

        Example example = MapperUtils.andEqualToWithSingleValue(ExamineLog.class, "spuId", spuId);
        List<ExamineLog> examineLogList = examineLogMapper.selectByExample(example);
        if(examineLogList==null || examineLogList.size()==0){
            examineLog.setCreateTime(date);
            examineLog.setSpuId(spuId);
            examineLogMapper.insertSelective(examineLog);
            examineId = examineLog.getId();// In order to use this, must set the Options in mapper
        }else{
            examineLog.setUpdateTime(date);
            examineId = examineLogList.get(0).getId();
            examineLog.setId(examineId);
            examineLogMapper.updateByPrimaryKeySelective(examineLog);
        }
        // save the examine log record

        System.out.println("examineId: "+examineId);
        ExamineLogRecord examineLogRecord = new ExamineLogRecord();
        examineLogRecord.setExamineId(examineId);
        examineLogRecord.setStatus("1");
        examineLogRecord.setMemo(memo);
        examineLogRecordMapper.insertSelective(examineLogRecord);

    }

    @Override
    @Transactional
    public void batchExamineByIds(String[] spuIds, String radio, String memo) {
        for (String spuId : spuIds) {
            updateSpuViaExamine(spuId, radio, memo);
        }
    }

    /**
     * 构建查询条件
     * @param searchMap
     * @return
     */
    private Example createExample(Map<String, Object> searchMap){
        Example example=new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        if(searchMap!=null){
            // 主键
            if(searchMap.get("id")!=null && !"".equals(searchMap.get("id"))){
                criteria.andLike("id","%"+searchMap.get("id")+"%");
            }
            // 货号
            if(searchMap.get("sn")!=null && !"".equals(searchMap.get("sn"))){
                criteria.andLike("sn","%"+searchMap.get("sn")+"%");
            }
            // SPU名
            if(searchMap.get("name")!=null && !"".equals(searchMap.get("name"))){
                criteria.andLike("name","%"+searchMap.get("name")+"%");
            }
            // 副标题
            if(searchMap.get("caption")!=null && !"".equals(searchMap.get("caption"))){
                criteria.andLike("caption","%"+searchMap.get("caption")+"%");
            }
            // 图片
            if(searchMap.get("image")!=null && !"".equals(searchMap.get("image"))){
                criteria.andLike("image","%"+searchMap.get("image")+"%");
            }
            // 图片列表
            if(searchMap.get("images")!=null && !"".equals(searchMap.get("images"))){
                criteria.andLike("images","%"+searchMap.get("images")+"%");
            }
            // 售后服务
            if(searchMap.get("saleService")!=null && !"".equals(searchMap.get("saleService"))){
                criteria.andLike("saleService","%"+searchMap.get("saleService")+"%");
            }
            // 介绍
            if(searchMap.get("introduction")!=null && !"".equals(searchMap.get("introduction"))){
                criteria.andLike("introduction","%"+searchMap.get("introduction")+"%");
            }
            // 规格列表
            if(searchMap.get("specItems")!=null && !"".equals(searchMap.get("specItems"))){
                criteria.andLike("specItems","%"+searchMap.get("specItems")+"%");
            }
            // 参数列表
            if(searchMap.get("paraItems")!=null && !"".equals(searchMap.get("paraItems"))){
                criteria.andLike("paraItems","%"+searchMap.get("paraItems")+"%");
            }
            // 是否上架
            if(searchMap.get("isMarketable")!=null && !"".equals(searchMap.get("isMarketable"))){
                criteria.andLike("isMarketable","%"+searchMap.get("isMarketable")+"%");
            }
            // 是否启用规格
            if(searchMap.get("isEnableSpec")!=null && !"".equals(searchMap.get("isEnableSpec"))){
                criteria.andLike("isEnableSpec","%"+searchMap.get("isEnableSpec")+"%");
            }
            // 是否删除
            if(searchMap.get("isDelete")!=null && !"".equals(searchMap.get("isDelete"))){
                criteria.andLike("isDelete","%"+searchMap.get("isDelete")+"%");
            }
            // 审核状态
            if(searchMap.get("status")!=null && !"".equals(searchMap.get("status"))){
                criteria.andLike("status","%"+searchMap.get("status")+"%");
            }

            // 品牌ID
            if(searchMap.get("brandId")!=null ){
                criteria.andEqualTo("brandId",searchMap.get("brandId"));
            }
            // 一级分类
            if(searchMap.get("category1Id")!=null ){
                criteria.andEqualTo("category1Id",searchMap.get("category1Id"));
            }
            // 二级分类
            if(searchMap.get("category2Id")!=null ){
                criteria.andEqualTo("category2Id",searchMap.get("category2Id"));
            }
            // 三级分类
            if(searchMap.get("category3Id")!=null ){
                criteria.andEqualTo("category3Id",searchMap.get("category3Id"));
            }
            // 模板ID
            if(searchMap.get("templateId")!=null ){
                criteria.andEqualTo("templateId",searchMap.get("templateId"));
            }
            // 运费模板id
            if(searchMap.get("freightId")!=null ){
                criteria.andEqualTo("freightId",searchMap.get("freightId"));
            }
            // 销量
            if(searchMap.get("saleNum")!=null ){
                criteria.andEqualTo("saleNum",searchMap.get("saleNum"));
            }
            // 评论数
            if(searchMap.get("commentNum")!=null ){
                criteria.andEqualTo("commentNum",searchMap.get("commentNum"));
            }

        }
        return example;
    }

}
