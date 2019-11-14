package com.ayang818.agito.helper;

import com.ayang818.agito.annotation.Inject;
import com.ayang818.agito.util.ReflectionUtil;

import java.lang.reflect.Field;
import java.util.Map;

public final class IocHelper {
    static {
        // 对于agito中的Bean，里面的使用Inject Annotation修饰对象就会被注入
        Map<Class<?>, Object> beanMap = BeanHelper.getBeanMap();
        if (!beanMap.isEmpty()) {
            for (Map.Entry<Class<?>, Object> beanEntry : beanMap.entrySet()) {
                Class<?> cls = beanEntry.getKey();
                Object object = beanEntry.getValue();
                // 通过反射获取对象中的所有字段
                Field[] declaredFields = cls.getDeclaredFields();
                for (Field field : declaredFields) {
                    if (field.isAnnotationPresent(Inject.class)) {
                        Class<?> classType = field.getType();
                        Object instance = beanMap.get(classType);
                        if (instance != null) {
                            ReflectionUtil.setFields(object, field, instance);
                        }
                    }
                }
            }
        }
    }
}