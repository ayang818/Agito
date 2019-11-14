package com.ayang818.agito.cglibtest.proxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @ClassName CGlibProxy
 * @Dessription TODO
 * @Author 杨丰畅
 * @Date 2019/11/14 13:01
 **/
public class CGlibProxy implements MethodInterceptor {

    private static CGlibProxy cGlibProxy = new CGlibProxy();

    public static CGlibProxy getInstance() {
        return cGlibProxy;
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        before();
        Object result = methodProxy.invokeSuper(obj, args);
        after();
        return result;
    }


    private void after() {
        System.out.println("finished");
    }

    private void before() {
        System.out.println("start");
    }

    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> cls) {
        return (T) Enhancer.create(cls, this);
    }
}
