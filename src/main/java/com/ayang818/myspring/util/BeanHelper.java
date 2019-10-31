package com.ayang818.myspring.util;


import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class BeanHelper {
    private static final Map<Class<?>, Object> BEAN_MAP = new HashMap<>();

    static {
        Set<Class<?>> beanClassSet = ClassHelper.getBeanClassSet();
        for (Class<?> aClass : beanClassSet) {
            Object object = ReflectionUtil.newInstance(aClass);
            BEAN_MAP.put(aClass, object);
        }
    }

    public static Map<Class<?>, Object> getBeanMap() {
        return BEAN_MAP;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(Class<?> cls) {
        if (!BEAN_MAP.containsKey(cls)) {
            throw new RuntimeException("can not get bean of by class : " + cls);
        }
        return (T) BEAN_MAP.get(cls);
    }
}
