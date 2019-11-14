package com.ayang818.agito.helper;

import com.ayang818.agito.annotation.Action;
import com.ayang818.agito.util.Handler;
import com.ayang818.agito.util.Request;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName ControllerHelper
 * @Dessription TODO
 * @Author 杨丰畅
 * @Date 2019/11/5 19:19
 **/
public class ControllerHelper {
    public static final Map<Request, Handler> ACTION_MAP = new HashMap<>(16);

    static {
        Set<Class<?>> controllerClassSet = ClassHelper.getControllerClassSet();
        if (!controllerClassSet.isEmpty()) {
            controllerClassSet.forEach(controllerClass -> {
                Method[] declaredMethods = controllerClass.getDeclaredMethods();
                for (Method method : declaredMethods) {
                    if (method.isAnnotationPresent(Action.class)) {
                        Action annotation = method.getAnnotation(Action.class);
                        String mapping = annotation.value();
                        String httpMethod = annotation.method();
                        Request request = new Request(httpMethod, mapping);
                        Handler handler = new Handler(controllerClass, method);
                        ACTION_MAP.put(request, handler);
                    }
                }
            });
        }
    }


    public static Handler getHandler(String path, String method) {
        Request request = new Request(path, method);
        return ACTION_MAP.get(request);
    }

}
