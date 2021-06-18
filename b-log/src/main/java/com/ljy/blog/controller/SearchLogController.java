package com.ljy.blog.controller;

import lombok.extern.java.Log;
import org.elasticsearch.action.search.SearchAction;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Log
@RestController
@RequestMapping(value = "/search")
public class SearchLogController {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @RequestMapping(value = "search")
    public void search(@RequestParam(value = "business",required = true)String business,@RequestParam(value = "host",required = false) String host
            ,@RequestParam(value = "startTime",required = false)String startTime,@RequestParam(value = "endTime",required = false)String endTime
            ,@RequestParam(value = "timeZone",required = false,defaultValue = "en") String timeZone,@RequestParam(value = "timeField",required = true,defaultValue = "@timestamp") String timeField
            ,@RequestParam(value = "from",required = false,defaultValue = "0") Integer from,@RequestParam(value = "to",required = false,defaultValue = "20") Integer to){
        SearchRequestBuilder ssb=new SearchRequestBuilder(restHighLevelClient, SearchAction.);
        ssb=ssb.setIndices(createIndices(business,startTime,endTime).toArray(new String[]{})).setPostFilter(QueryBuilders.boolQuery().filter(QueryBuilders.rangeQuery("@timestamp")))


    }


    private List<String> createIndices(String prefix,String startTime,String endTime){



    }





}
