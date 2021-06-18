package com.ljy.blog.config;

import lombok.extern.java.Log;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Log
@Configuration
public class ElasticSearchConfig {

    @Bean
    public RestHighLevelClient restHighLevelClient(@Autowired NacosConfig nacosConfig){
        RestClientBuilder builder=null;
        builder=RestClient.builder(generateHostList(nacosConfig).toArray(new HttpHost[]{}));
        RestHighLevelClient restHighLevelClient=new RestHighLevelClient(builder);
        return restHighLevelClient;
    }


    private List<HttpHost> generateHostList(NacosConfig nacosConfig){
        List<String> hosts=nacosConfig.getEshost();
        List<Integer> ports=nacosConfig.getEsPort();
        List<String> schemeNames=nacosConfig.getEsSchemeName();
        List<HttpHost> results=new ArrayList<>();
        Stream.iterate(0, i->i+1).limit(hosts==null?0:hosts.size()).forEach((i)->{
            results.add(new HttpHost(hosts.get(i),ports.get(i),schemeNames.get(i)));
        });
        return results;
    }

}
