package com.ayang818.agito.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Date 21:01 2019/10/22
 * @Author 杨丰畅
 * @Description value值路径，method指http请求方法
 * @Param
 * @Return
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Action {
    String value();
    String method();
}
