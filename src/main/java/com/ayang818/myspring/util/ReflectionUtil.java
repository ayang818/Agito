package com.ayang818.myspring.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionUtil {
    private static final Logger LOGGER =  LoggerFactory.getLogger(ReflectionUtil.class);

    public static Object newInstance(Class<?> cls) {
        Object instance = null;
        try {
            instance = cls.getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            LOGGER.error(cls.getName() + " failed to newInstance");
            throw new RuntimeException(e);
        }
        return instance;
    }

    public static Object newInstance(Class<?> cls, Class<?> ...args) {
        Object instance = null;
        try {
            instance = cls.getDeclaredConstructor().newInstance(args);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return instance;
    }

    public static Object invokeMethod(Object obj, Method method, Object ...args) {
        Object result = null;
        try {
            method.setAccessible(true);
            result = method.invoke(obj, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            LOGGER.error(method.getName() + " invoke failed");
            throw  new RuntimeException(e);
        }
        return result;
    }

    public static void setFields(Object obj, Field field, Object value) {
        try {
            field.setAccessible(true);
            field.set(obj, value);
        } catch (IllegalAccessException e) {
            LOGGER.error(field.getName() + " failed set value");
            throw new RuntimeException(e);
        }
    }

}
