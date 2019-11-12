package com.ayang818.myspring.servlet;

import com.ayang818.myspring.annotation.responseAnnotation.ResponseBody;
import com.ayang818.myspring.annotation.responseAnnotation.ResponseView;
import com.ayang818.myspring.helper.BeanHelper;
import com.ayang818.myspring.helper.ControllerHelper;
import com.ayang818.myspring.helper.InitHelper;
import com.ayang818.myspring.util.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Optional;

public class DispatchServlet extends HttpServlet implements Serviceable {
    @Override
    public void init() {
        InitHelper.init();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Object result = mappingAndInvoke(request, response);
    }

    @Override
    protected  void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Object result = mappingAndInvoke(request, response);
    }

    @Override
    public Object mappingAndInvoke(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pathInfo = request.getPathInfo();
        String method = request.getMethod().toLowerCase();
        Handler handler = ControllerHelper.getHandler(pathInfo, method);
        if (handler != null) {
            Class<?> controllerClass = handler.getControllerClass();
            Object bean = BeanHelper.getBean(controllerClass);
            HashMap<String, Object> paramMap = new HashMap<>();
            Enumeration<String> parameterNames = request.getParameterNames();
            // 获取表单中的数据
            while (parameterNames.hasMoreElements()) {
                String paramName = parameterNames.nextElement();
                String[] parameterValue = request.getParameterValues(paramName);
                paramMap.put(paramName, parameterValue);
            }
            // 获取POST携带的数据
            String body = CodecUtil.decodeURL(StreamUtil.getString(request.getInputStream()));
            if (!body.isEmpty()) {
                String[] params = body.split("&");
                Optional.of(params).map(paramList -> {
                    for (String paramKV : paramList) {
                        String[] param = paramKV.split("=");
                        if (param.length == 2) {
                            paramMap.put(param[0], param[1]);
                        }
                    }
                    return null;
                });
            }
            // 获取URL参数
            StringBuffer requestURL = request.getRequestURL();
            String[] params = requestURL.toString().split("/?", 1);
            String paramList = params.length == 2 ? params[1] : null;
            Optional.ofNullable(paramList).map(param -> {
                String[] paramKVList = param.split("&");
                for (String paramKV : paramKVList) {
                    String[] entryList = paramKV.split("=", 1);
                    if (entryList.length == 2) {
                        paramMap.put(entryList[0], entryList[1]);
                    }
                }
                return null;
            });
            // 使用反射调用映射函数
            Param param = new Param(paramMap);
            Method actionMethod = handler.getActionMethod();
            Object result = ReflectionUtil.invokeMethod(bean, actionMethod, param);
            // 判断返回类型注解注解，返回视图
            if (actionMethod.isAnnotationPresent(ResponseView.class)) {
                return viewResponse(result);
            }
            // 返回Json数据
            if (actionMethod.isAnnotationPresent(ResponseBody.class)) {
                return jsonResponse(result);
            }
         }
        return null;
    }

    @Override
    public String jsonResponse(Object jsonObject) {
        // 枚举类解析策略
        if (jsonObject.getClass().isEnum()) {
            return JsonUtil.parseEnumToJson(jsonObject);
        }
        return JsonUtil.parseObjectToJson(jsonObject);
    }

    @Override
    public Object viewResponse(Object view) {
        return null;
    }

}
