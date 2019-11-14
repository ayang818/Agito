package com.ayang818.agito.util;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * @ClassName Request
 * @Dessription 作为HashMap中的键，所以要重写hashcode和equals的方法
 * @Author 杨丰畅
 * @Date 2019/11/5 19:07
 **/
public class Request {
    private String requestMethod;

    private String requestPath;

    public Request(String requestMethod, String requestPath) {
        this.requestMethod = requestMethod;
        this.requestPath = requestPath;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public String getRequestPath() {
        return requestPath;
    }

    // 重写equals必须重写hashCode
    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }
}
