package com.ayang818.myspring.servlet;

import java.util.HashMap;
import java.util.Map;

public class Param {
    private Map<String, Object> paramMap = new HashMap<>();

    public Param(Map<String, Object> paramMap) {
        this.paramMap = paramMap;
    }

    public Object getField(String key) {
        return paramMap.get(key);
    }
}
