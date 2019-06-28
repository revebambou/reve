package cn.reve.service.impl;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import cn.reve.dao.SkuMapper;
import cn.reve.entity.PageResult;
import cn.reve.pojo.goods.Sku;
import cn.reve.service.goods.SkuService;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import tk.mybatis.mapper.entity.Example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(timeout = 5000)
public class SkuServiceImpl implements SkuService {

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Value("${httpHost.index}")
    private String index;

    @Value("${httpHost.name}")
    private String name;

    @Value("${httpHost.brandName}")
    private String brandName;

    @Value("${httpHost.categoryName}")
    private String categoryName;

    @Value("${httpHost.spec}")
    private String spec;

    @Value("${httpHost.price}")
    private String price;
    /**
     * 返回全部记录
     * @return
     */
    public List<Sku> findAll() {
        return skuMapper.selectAll();
    }

    /**
     * 分页查询
     * @param page 页码
     * @param size 每页记录数
     * @return 分页结果
     */
    public PageResult<Sku> findPage(int page, int size) {
        PageHelper.startPage(page,size);
        Page<Sku> skus = (Page<Sku>) skuMapper.selectAll();
        return new PageResult<Sku>(skus.getTotal(),skus.getResult());
    }

    /**
     * 条件查询
     * @param searchMap 查询条件
     * @return
     */
    public List<Sku> findList(Map<String, Object> searchMap) {
        Example example = createExample(searchMap);
        return skuMapper.selectByExample(example);
    }

    /**
     * 分页+条件查询
     * @param searchMap
     * @param page
     * @param size
     * @return
     */
    public PageResult<Sku> findPage(Map<String, Object> searchMap, int page, int size) {
        PageHelper.startPage(page,size);
        Example example = createExample(searchMap);
        Page<Sku> skus = (Page<Sku>) skuMapper.selectByExample(example);
        return new PageResult<Sku>(skus.getTotal(),skus.getResult());
    }

    /**
     * 根据Id查询
     * @param id
     * @return
     */
    public Sku findById(String id) {
        return skuMapper.selectByPrimaryKey(id);
    }

    /**
     * 新增
     * @param sku
     */
    public void add(Sku sku) {
        skuMapper.insert(sku);
    }

    /**
     * 修改
     * @param sku
     */
    public void update(Sku sku) {
        skuMapper.updateByPrimaryKeySelective(sku);
    }

    /**
     *  删除
     * @param id
     */
    public void delete(String id) {
        skuMapper.deleteByPrimaryKey(id);
    }

    @Override
    public Map searchMap(Map<String, String> searchMap) {
        //package query request
        SearchRequest searchRequest = new SearchRequest(index);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();

        //set brand
        String brand_name = searchMap.get(brandName);
        if(brand_name != null && !"".equals(brand_name)){
            TermQueryBuilder termQueryBuilder = new TermQueryBuilder(brandName, searchMap.get(brandName));
            boolQueryBuilder.must(termQueryBuilder);
        }

        //set category
        String category_name = searchMap.get(categoryName);
        if(category_name!=null && !"".equals(category_name)){
            TermQueryBuilder termQueryBuilder = new TermQueryBuilder(categoryName, searchMap.get(categoryName));
            boolQueryBuilder.must(termQueryBuilder);
        }

        //set search value
        MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder(name, searchMap.get(name));
        boolQueryBuilder.must(matchQueryBuilder);

        searchSourceBuilder.query(boolQueryBuilder);

        //group by category
        TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders.terms("category").field(categoryName);
        searchSourceBuilder.aggregation(termsAggregationBuilder);

        //group by brand
        TermsAggregationBuilder termsBrandAggregationBuilder = AggregationBuilders.terms("brand").field(brandName);
        searchSourceBuilder.aggregation(termsBrandAggregationBuilder);

        //filter by price
        String filterPrice = searchMap.get(price);
        if(filterPrice!=null && !"".equals(filterPrice)) {
            String[] prices = filterPrice.split("-");

            RangeQueryBuilder rangeQueryBuilder = null;
            if (!"0".equals(prices[0])) {
                rangeQueryBuilder = new RangeQueryBuilder("price").gte(prices[0] + "00");
                //if here is not assign value to the boolQueryBuilder, the value will be override when assigning again
                boolQueryBuilder.filter(rangeQueryBuilder);
            }
            if (!"*".equals(prices[1])) {
                rangeQueryBuilder = new RangeQueryBuilder("price").lte(prices[1] + "00");
                boolQueryBuilder.filter(rangeQueryBuilder);
            }
        }
        searchRequest.source(searchSourceBuilder);

        Map map = new HashMap();
        //get response result
        try {
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            SearchHits hits = searchResponse.getHits();
            List<Map<String, Object>> resultList = new ArrayList<>();
            for(SearchHit hit:hits){
                Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                resultList.add(sourceAsMap);
            }

            //get categories and brands
            Aggregations aggregations = searchResponse.getAggregations();
            Map<String, Aggregation> asMap = aggregations.getAsMap();

            //Get categoryList
            Terms terms = (Terms) asMap.get("category");

            if(terms!=null) {
                List<String> categoryList = new ArrayList<String>();
                List<? extends Terms.Bucket> buckets = terms.getBuckets();
                for (Terms.Bucket bucket : buckets) {
                    String category = bucket.getKeyAsString();
                    categoryList.add(category);
                }
                map.put("categoryList", categoryList);
            }

            //Get brandList
            Terms termsBrand = (Terms) asMap.get("brand");
            if(termsBrand!=null){
                List<String> brandList = new ArrayList<String>();
                List<? extends Terms.Bucket> brandBuckets = termsBrand.getBuckets();
                for (Terms.Bucket brandBucket : brandBuckets) {
                    brandList.add(brandBucket.getKeyAsString());
                }
                map.put("brandList", brandList);
            }

            map.put("rows", resultList);
//            restHighLevelClient.close();//why there needn't to be closed?// because this created by spring when booting the server, if closed, it won't spawn
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 构建查询条件
     * @param searchMap
     * @return
     */
    private Example createExample(Map<String, Object> searchMap){
        Example example=new Example(Sku.class);
        Example.Criteria criteria = example.createCriteria();
        if(searchMap!=null){
            // 商品id
            if(searchMap.get("id")!=null && !"".equals(searchMap.get("id"))){
                criteria.andLike("id","%"+searchMap.get("id")+"%");
            }
            // 商品条码
            if(searchMap.get("sn")!=null && !"".equals(searchMap.get("sn"))){
                criteria.andLike("sn","%"+searchMap.get("sn")+"%");
            }
            // SKU名称
            if(searchMap.get("name")!=null && !"".equals(searchMap.get("name"))){
                criteria.andLike("name","%"+searchMap.get("name")+"%");
            }
            // 商品图片
            if(searchMap.get("image")!=null && !"".equals(searchMap.get("image"))){
                criteria.andLike("image","%"+searchMap.get("image")+"%");
            }
            // 商品图片列表
            if(searchMap.get("images")!=null && !"".equals(searchMap.get("images"))){
                criteria.andLike("images","%"+searchMap.get("images")+"%");
            }
            // SPUID
            if(searchMap.get("spuId")!=null && !"".equals(searchMap.get("spuId"))){
                criteria.andLike("spuId","%"+searchMap.get("spuId")+"%");
            }
            // 类目名称
            if(searchMap.get("categoryName")!=null && !"".equals(searchMap.get("categoryName"))){
                criteria.andLike("categoryName","%"+searchMap.get("categoryName")+"%");
            }
            // 品牌名称
            if(searchMap.get("brandName")!=null && !"".equals(searchMap.get("brandName"))){
                criteria.andLike("brandName","%"+searchMap.get("brandName")+"%");
            }
            // 规格
            if(searchMap.get("spec")!=null && !"".equals(searchMap.get("spec"))){
                criteria.andLike("spec","%"+searchMap.get("spec")+"%");
            }
            // 商品状态 1-正常，2-下架，3-删除
            if(searchMap.get("status")!=null && !"".equals(searchMap.get("status"))){
                criteria.andLike("status","%"+searchMap.get("status")+"%");
            }

            // 价格（分）
            if(searchMap.get("price")!=null ){
                criteria.andEqualTo("price",searchMap.get("price"));
            }
            // 库存数量
            if(searchMap.get("num")!=null ){
                criteria.andEqualTo("num",searchMap.get("num"));
            }
            // 库存预警数量
            if(searchMap.get("alertNum")!=null ){
                criteria.andEqualTo("alertNum",searchMap.get("alertNum"));
            }
            // 重量（克）
            if(searchMap.get("weight")!=null ){
                criteria.andEqualTo("weight",searchMap.get("weight"));
            }
            // 类目ID
            if(searchMap.get("categoryId")!=null ){
                criteria.andEqualTo("categoryId",searchMap.get("categoryId"));
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
