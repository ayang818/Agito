package com.ayang818.agito.helper;

import com.ayang818.agito.util.ClassUtil;

public class InitHelper {

    // 集中初始化工具类
    public static void init() {
        Class[] classSet = new Class[]{BeanHelper.class, ClassHelper.class, ControllerHelper.class, IocHelper.class};
        for (Class aClass : classSet) {
            ClassUtil.loadClass(aClass.getName(), false);
        }
    }
}
