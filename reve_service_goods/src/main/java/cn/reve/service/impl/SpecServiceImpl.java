package cn.reve.service.impl;
import cn.reve.dao.TemplateMapper;
import cn.reve.pojo.goods.Template;
import cn.reve.utils.MapperUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import cn.reve.dao.SpecMapper;
import cn.reve.entity.PageResult;
import cn.reve.pojo.goods.Spec;
import cn.reve.service.goods.SpecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;

@Service(interfaceClass = SpecService.class)
@Transactional
public class SpecServiceImpl implements SpecService {

    @Autowired
    private SpecMapper specMapper;

    @Autowired
    private TemplateMapper templateMapper;

    /**
     * 返回全部记录
     * @return
     */
    public List<Spec> findAll() {
        return specMapper.selectAll();
    }

    /**
     * 分页查询
     * @param page 页码
     * @param size 每页记录数
     * @return 分页结果
     */
    public PageResult<Spec> findPage(int page, int size) {
        PageHelper.startPage(page,size);
        Page<Spec> specs = (Page<Spec>) specMapper.selectAll();
        return new PageResult<Spec>(specs.getTotal(),specs.getResult());
    }

    /**
     * 条件查询
     * @param searchMap 查询条件
     * @return
     */
    public List<Spec> findList(Map<String, Object> searchMap) {
        Example example = createExample(searchMap);
        return specMapper.selectByExample(example);
    }

    /**
     * 分页+条件查询
     * @param searchMap
     * @param page
     * @param size
     * @return
     */
    public PageResult<Spec> findPage(Map<String, Object> searchMap, int page, int size) {
        PageHelper.startPage(page,size);
        Example example = createExample(searchMap);
        Page<Spec> specs = (Page<Spec>) specMapper.selectByExample(example);
        return new PageResult<Spec>(specs.getTotal(),specs.getResult());
    }

    /**
     * 根据Id查询
     * @param id
     * @return
     */
    public Spec findById(Integer id) {
        return specMapper.selectByPrimaryKey(id);
    }

    /**
     * 新增
     * @param spec
     */
    public void add(Spec spec) {
        specMapper.insert(spec);
        updateSpecNum(spec, 1);
    }

    /**
     * 修改
     * @param spec
     */
    public void update(Spec spec) {
        specMapper.updateByPrimaryKeySelective(spec);
    }

    /**
     *  删除
     * @param id
     */
    public void delete(Integer id) {
        Spec spec = specMapper.selectByPrimaryKey(id);
        updateSpecNum(spec, -1);

        specMapper.deleteByPrimaryKey(id);

    }

    @Override
    public PageResult<Spec> findSpecsByTemplateId(int size, int currentPage, int id) {
        PageHelper.startPage(currentPage, size);
        Example example = MapperUtils.andEqualToWithSingleValue(Spec.class, "templateId", id);
        Page<Spec> specPage = (Page<Spec>)specMapper.selectByExample(example);
        PageResult<Spec> specPageResult = new PageResult<>();
        specPageResult.setRows(specPage.getResult());
        specPageResult.setTotal(specPage.getTotal());
        return specPageResult;

    }

    /**
     * 构建查询条件
     * @param searchMap
     * @return
     */
    private Example createExample(Map<String, Object> searchMap){
        Example example=new Example(Spec.class);
        Example.Criteria criteria = example.createCriteria();
        if(searchMap!=null){
            // 名称
            if(searchMap.get("name")!=null && !"".equals(searchMap.get("name"))){
                criteria.andLike("name","%"+searchMap.get("name")+"%");
            }
            // 规格选项
            if(searchMap.get("options")!=null && !"".equals(searchMap.get("options"))){
                criteria.andLike("options","%"+searchMap.get("options")+"%");
            }
        }
        return example;
    }

    /**
     *update the specNum in the tb_template table depends on paraValue
     * @param spec
     * @param paraValue
     */
    private void updateSpecNum(Spec spec, int paraValue){
        int templateId = spec.getTemplateId();
        Template template = templateMapper.selectByPrimaryKey(templateId);
        int specNum = template.getSpecNum()+paraValue;
        template.setSpecNum(specNum);
        templateMapper.updateByPrimaryKeySelective(template);
    }

}
