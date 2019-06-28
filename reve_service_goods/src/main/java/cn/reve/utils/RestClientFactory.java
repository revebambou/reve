package cn.reve.utils;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:httpHost.properties")
public class RestClientFactory {

    @Value("${httpHost.url}")
    private String url;
    @Value("${httpHost.port}")
    private int port;

    //in order to use this bean, must scan this class or the (father) directory of this class
    @Bean(name="restHighLevelClient")
    public RestHighLevelClient getRestHighLevelClient(){
        HttpHost httpHost = new HttpHost(url, port, "http");
        RestClientBuilder clientBuilder = RestClient.builder(httpHost);
        return new RestHighLevelClient(clientBuilder);
    }
}
