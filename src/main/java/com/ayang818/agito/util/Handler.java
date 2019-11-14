package com.ayang818.agito.util;

import java.lang.reflect.Method;

/**
 * @ClassName Handler
 * @Dessription TODO
 * @Author 杨丰畅
 * @Date 2019/11/5 19:15
 **/
public class Handler {
    private Class<?> controllerClass;

    private Method actionMethod;

    public Handler(Class<?> controllerClass, Method actionMethod) {
        this.controllerClass = controllerClass;
        this.actionMethod = actionMethod;
    }

    public Class<?> getControllerClass() {
        return this.controllerClass;
    }

    public Method getActionMethod() {
        return this.actionMethod;
    }

    @Override
    public String toString() {
        return actionMethod.getName();
    }
}
