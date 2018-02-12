package com.spring.test.es.domain;

import java.io.Serializable;
import java.util.Map;

/**
 * @author sunboyu
 * @date 2018/2/11
 */
public class EsConfigBean implements Serializable {

    private static final long serialVersionUID = 3565544135397353778L;

    private String index;
    private String type;
    private String id;
    private Map<String, Object> map;

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, Object> getMap() {
        return map;
    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }
}