package com.ayang818.myspring.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtil.class);

    public static String parseEnumToJson(Object object) {
        SerializeConfig config = new SerializeConfig();
        try {
            config.configEnumAsJavaBean((Class<? extends Enum>) object.getClass());
        } catch (ClassCastException e) {
            LOGGER.error("cast class" + object.getClass().toString() + "error");
            throw new RuntimeException(e);
        }
        return JSON.toJSONString(object, config);
    }

    public static String parseObjectToJson(Object object) {
        return JSON.toJSONString(object);
    }

    public static <T> T parseJsonToObject(String json, T object) {
        return (T) JSON.parseObject(json, object.getClass());
    }
}