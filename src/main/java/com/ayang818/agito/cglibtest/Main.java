package com.ayang818.agito.cglibtest;

import com.ayang818.agito.cglibtest.proxy.CGlibProxy;

/**
 * @ClassName Main
 * @Dessription TODO
 * @Author 杨丰畅
 * @Date 2019/11/14 17:32
 **/
public class Main {
    public static void main(String[] args) {
        HelloImpl enhancedHello = CGlibProxy.getInstance().getProxy(HelloImpl.class);
        enhancedHello.say();
    }
}