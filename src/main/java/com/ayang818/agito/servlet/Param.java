package com.ayang818.agito.servlet;

import java.util.Map;

public class Param {
    private Map<String, Object> paramMap;

    public Param(Map<String, Object> paramMap) {
        this.paramMap = paramMap;
    }

    public Object getField(String key) {
        return paramMap.get(key);
    }

    public Map<String, Object> getMap() {
        return this.paramMap;
    }
}
