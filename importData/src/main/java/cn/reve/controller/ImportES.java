package cn.reve.controller;

import cn.reve.entity.PageResult;
import cn.reve.pojo.goods.Sku;
import cn.reve.service.goods.SkuService;
import cn.reve.utils.IdWorker;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ImportES {

    @Reference
    private SkuService skuService;

    @Autowired
    private IdWorker idWorker;

    @GetMapping("/importData")
    public String importData() {

        int pageNum = 1;
        int pageSize = 100;
        //loop 910 times and all the data in sku database is saved to the es
        // pageSize: 100===pageNum: 910
        while(true) {
            PageResult<Sku> page = skuService.findPage(pageNum, pageSize);
            List<Sku> skuList = page.getRows();
            System.out.println(skuList);

            if (skuList != null && skuList.size() != 0) {
                HttpHost httpHost = new HttpHost("127.0.0.1", 9200, "http");
                RestClientBuilder restClientBuilder = RestClient.builder(httpHost);
                RestHighLevelClient restHighLevelClient = new RestHighLevelClient(restClientBuilder);


                BulkRequest bulkRequest = new BulkRequest();
                for (Sku sku : skuList) {
                    IndexRequest indexRequest = new IndexRequest("sku", "doc", idWorker.nextId() + "");
                    Map map = saveSkuToMap(sku);
                    indexRequest.source(map);
                    bulkRequest.add(indexRequest);
                }
                try {
                    BulkResponse bulkItemResponses = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
                    int status = bulkItemResponses.status().getStatus();
                    System.out.println(status);
                    restHighLevelClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    pageNum++;
                }
            } else {
                System.out.println("sku list is null");
                break;
            }
        }
        return "pageSize: "+pageSize+"==="+"pageNum: "+pageNum;
    }

    public static Map saveSkuToMap(Sku sku){
        Map skuMap = new HashMap();
        skuMap.put("name", sku.getName());
        skuMap.put("price", sku.getPrice());
        skuMap.put("image", sku.getImage());
        skuMap.put("createTime", sku.getCreateTime());
        skuMap.put("spuId", sku.getSpuId());
        skuMap.put("categoryName", sku.getCategoryName());
        skuMap.put("brandName", sku.getBrandName());
        skuMap.put("saleNum", sku.getSaleNum());
        skuMap.put("commentNum", sku.getCommentNum());
        String spec = sku.getSpec();
        Map map = JSON.parseObject(spec);
        skuMap.put("spec", map);
        return skuMap;
    }
}
