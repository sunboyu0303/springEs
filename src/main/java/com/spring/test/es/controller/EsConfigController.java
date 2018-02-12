package com.spring.test.es.controller;

import com.spring.test.es.domain.EsConfigBean;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author sunboyu
 * @date 2018/2/11
 */
@RestController
@RequestMapping("/config/es")
public class EsConfigController {

    @Autowired
    private TransportClient client;

    @PostMapping
    public IndexResponse create(@RequestBody EsConfigBean esConfigBean) {

        String index = esConfigBean.getIndex();
        String type = esConfigBean.getType();
        String id = esConfigBean.getId();
        Map<String, Object> map = esConfigBean.getMap();

        return client.prepareIndex().setIndex(index).setType(type).setId(id).setSource(map).execute().actionGet();
    }

    @GetMapping
    public GetResponse get(String index, String type, String id) {
        return client.prepareGet().setIndex(index).setType(type).setId(id).execute().actionGet();
    }

    @GetMapping("/list")
    public List<String> getList(String index, String type) {
        QueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("skuId").gte("0000004");
        SearchResponse searchResponse = client.prepareSearch().setIndices(index)
                .setTypes(type)
                .setQuery(rangeQueryBuilder)
                .addSort("skuId", SortOrder.DESC)
                .setSize(2)
                .execute()
                .actionGet();
        List<String> list = new ArrayList<>();
        for (SearchHit searchHit : searchResponse.getHits()) {
            list.add(searchHit.getId());
        }
        return list;
    }

    @GetMapping("/page")
    public List<String> getPage(String index, String type, int page, int pageSize) {

        QueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("skuId").gte("0000004");
        int _index = 1;
        TimeValue timeValue = new TimeValue(60000);

        SearchResponse scrollResp = client.prepareSearch()
                .setIndices(index)
                .setTypes(type)
                .setScroll(timeValue)
                .setQuery(rangeQueryBuilder)
                .addSort("skuId", SortOrder.DESC)
                .setSize(pageSize).get();

        List<String> list = new ArrayList<>();
        do {
            if (_index == page) {
                for (SearchHit searchHit : scrollResp.getHits()) {
                    list.add(searchHit.getId());
                }
                break;
            } else {
                _index++;
                scrollResp = client.prepareSearchScroll(scrollResp.getScrollId()).setScroll(timeValue).execute().actionGet();
            }
        } while (scrollResp.getHits().getHits().length != 0);

        return list;
    }
}