package cn.reve.controller;

import cn.reve.pojo.goods.Sku;
import cn.reve.utils.IdWorker;
import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext-service.xml")
public class TestImport {
    @Autowired
    private IdWorker idWorker;
    @Test
    public void testConnection(){
        HttpHost httpHost = new HttpHost("127.0.0.1", 9200, "http");
        RestClientBuilder restClientBuilder = RestClient.builder(httpHost);
        RestHighLevelClient restHighLevelClient = new RestHighLevelClient(restClientBuilder);

        //package query request
        SearchRequest searchRequest = new SearchRequest("sku");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("name","iphone 6");
        searchSourceBuilder.query(matchQueryBuilder);
        searchRequest.source(searchSourceBuilder);

        //get response result
        try {
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            SearchHits hits = searchResponse.getHits();
            System.out.println(hits.getTotalHits());
            for (SearchHit hit : hits) {
                System.out.println(hit.getSourceAsString());
            }
            restHighLevelClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testPostValue(){
        HttpHost httpHost = new HttpHost("127.0.0.1", 9200, "http");
        RestClientBuilder restClientBuilder = RestClient.builder(httpHost);
        RestHighLevelClient restHighLevelClient = new RestHighLevelClient(restClientBuilder);

        IndexRequest indexRequest = new IndexRequest("sku", "doc",  idWorker.nextId()+"");
        Map map = new HashMap();
        map.put("name","huawei3");
        map.put("brandName","huawei");
        indexRequest.source(map);

        try {
            IndexResponse indexResponse = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
            int status = indexResponse.status().getStatus();
            System.out.println(status);
            restHighLevelClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void batchPostValue(){
        HttpHost httpHost = new HttpHost("127.0.0.1", 9200, "http");
        RestClientBuilder restClientBuilder = RestClient.builder(httpHost);
        RestHighLevelClient restHighLevelClient = new RestHighLevelClient(restClientBuilder);

        List<Map> mapList = new ArrayList<>();
        Sku sku = new Sku();
        sku.setName("iphone 7");
        sku.setBrandName("apple");
        Sku sku2 = new Sku();
        sku2.setName("iphone 7s");
        sku2.setBrandName("apple");
        Map map1 = ImportES.saveSkuToMap(sku);
        Map map2 = ImportES.saveSkuToMap(sku2);
        mapList.add(map1);
        mapList.add(map2);

        BulkRequest bulkRequest = new BulkRequest();
        for (Map map : mapList) {
            IndexRequest indexRequest = new IndexRequest("sku", "doc", idWorker.nextId()+"");
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
        }
    }
}
