package com.ayang818.myspring.util;

import com.ayang818.myspring.annotation.Action;
import com.ayang818.myspring.annotation.Controller;
import com.ayang818.myspring.annotation.Service;

import java.util.HashSet;
import java.util.Set;

public final class ClassHelper {
    private static final Set<Class<?>> CLASS_SET;
    static {
        String basePackageName = ConfigHelper.getBasePackageName();
        CLASS_SET = ClassUtil.getClassSet(basePackageName);
    }

    public static Set<Class<?>> getServiceClassSet() {
        Set<Class<?>> serviceClassSet = new HashSet<>();
        for (Class<?> aClass : CLASS_SET) {
            if (aClass.isAnnotationPresent(Service.class)) {
                serviceClassSet.add(aClass);
            }
        }
        return serviceClassSet;
    }

    public static Set<Class<?>> getControllerClassSet() {
        Set<Class<?>> controllerClassSet = new HashSet<>();
        for (Class<?> aClass : CLASS_SET) {
            if (aClass.isAnnotationPresent(Controller.class)) {
                controllerClassSet.add(aClass);
            }
        }
        return controllerClassSet;
    }

    public static Set<Class<?>> getActionClassSet() {
        Set<Class<?>> actionClassSet = new HashSet<>();
        for (Class<?> aClass : CLASS_SET) {
            if (aClass.isAnnotationPresent(Action.class)) {
                actionClassSet.add(aClass);
            }
        }
        return actionClassSet;
    }

    public static Set<Class<?>> getBeanClassSet() {
        HashSet<Class<?>> beanClassSet = new HashSet<>();
        beanClassSet.addAll(getServiceClassSet());
        beanClassSet.addAll(getControllerClassSet());
        return beanClassSet;
    }
}
