package com.ayang818.myspring.servlet;

import com.ayang818.myspring.helper.BeanHelper;
import com.ayang818.myspring.helper.ControllerHelper;
import com.ayang818.myspring.helper.InitHelper;
import com.ayang818.myspring.util.CodecUtil;
import com.ayang818.myspring.util.Handler;
import com.ayang818.myspring.util.StreamUtil;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Optional;

public class DispatchServlet extends HttpServlet {
    @Override
    public void init() {
        InitHelper.init();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pathInfo = request.getPathInfo();
        String method = request.getMethod().toLowerCase();
        Handler handler = ControllerHelper.getHandler(pathInfo, method);
        if (handler != null) {
            Class<?> controllerClass = handler.getControllerClass();
            Object bean = BeanHelper.getBean(controllerClass);
            HashMap<String, Object> paramMap = new HashMap<>();
            Enumeration<String> parameterNames = request.getParameterNames();
            while (parameterNames.hasMoreElements()) {
                String paramName = parameterNames.nextElement();
                String[] parameterValue = request.getParameterValues(paramName);
                paramMap.put(paramName, parameterValue);
            }
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
        }
    }

    @Override
    protected  void doPost(HttpServletRequest request, HttpServletResponse response) {

    }
}
