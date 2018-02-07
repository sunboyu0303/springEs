package com.spring.test.es.controller;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * Created by sunboyu on 2018/2/7.
 */
@RestController
@RequestMapping("/test/es")
public class TestEsController {

    @Autowired
    private TransportClient client;

    @GetMapping("/save")
    public String index() {
        Map<String, Object> infoMap = new HashMap<String, Object>();
        infoMap.put("name", "广告信息11");
        infoMap.put("title", "我的广告22");
        infoMap.put("createTime", new Date());
        infoMap.put("count", 1022);

        IndexResponse indexResponse = client.prepareIndex("test", "info").setSource(infoMap).execute().actionGet();
        return indexResponse.getId();
    }

    @GetMapping("/get/{id}")
    public String get(@PathVariable String id) {
        GetResponse getResponse = client.prepareGet("test", "info", id).execute().actionGet();
        return getResponse.getSourceAsString();
    }

    @GetMapping("/query/{count}")
    public List<Map<String, Object>> query(@PathVariable int count) {

        QueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("count").gt(count);
        SearchResponse searchResponse = client.prepareSearch("test")
                .setTypes("info")
                .setQuery(rangeQueryBuilder)
                .addSort("createTime", SortOrder.ASC)
                .setSize(20)
                .execute()
                .actionGet();

        SearchHits searchHits = searchResponse.getHits();
        List<Map<String, Object>> list = new ArrayList<>();
        for (SearchHit searchHit : searchHits.getHits()) {
            list.add(searchHit.getSourceAsMap());
        }
        return list;
    }
}
