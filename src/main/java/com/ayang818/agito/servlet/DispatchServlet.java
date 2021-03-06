package com.ayang818.agito.servlet;

import com.ayang818.agito.annotation.responseAnnotation.ResponseBody;
import com.ayang818.agito.annotation.responseAnnotation.ResponseView;
import com.ayang818.agito.helper.BeanHelper;
import com.ayang818.agito.helper.ConfigHelper;
import com.ayang818.agito.helper.ControllerHelper;
import com.ayang818.agito.helper.InitHelper;
import com.ayang818.agito.servlet.response.Json;
import com.ayang818.agito.servlet.response.View;
import com.ayang818.agito.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@WebServlet(urlPatterns = "/*", loadOnStartup = 0)
public class DispatchServlet extends HttpServlet implements Serviceable {

    private static final Logger LOGGER = LoggerFactory.getLogger(DispatchServlet.class);

    @Override
    public void init(ServletConfig servletConfig) {
        InitHelper.init();
        ServletContext servletContext = servletConfig.getServletContext();
        // 注册处理HTML的Servlet
        ServletRegistration htmlServlet = servletContext.getServletRegistration("html");
        String appHTMLPath = ConfigHelper.getAppHTMLPath();
        appHTMLPath = !appHTMLPath.endsWith("/") && !appHTMLPath.endsWith("\\") ? appHTMLPath+"/" : appHTMLPath;
        htmlServlet.addMapping(appHTMLPath + "*");
        // 注册处理静态资源的Servlet
        ServletRegistration defaultServlet = servletContext.getServletRegistration("default");
        String appAssertPath = ConfigHelper.getAppAssertPath();
        appAssertPath = !appAssertPath.endsWith("/") && !appAssertPath.endsWith("\\") ? appAssertPath+"/" : appAssertPath;
        defaultServlet.addMapping(appAssertPath + "*");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            mappingAndInvoke(request, response);
        } catch (ServletException e) {
            LOGGER.error("get path error");
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            mappingAndInvoke(request, response);
        } catch (ServletException e) {
            LOGGER.error("post path error");
            e.printStackTrace();
        }
    }

    @Override
    public void mappingAndInvoke(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String pathInfo = request.getPathInfo();
        String method = request.getMethod().toLowerCase();
        Handler handler = ControllerHelper.getHandler(pathInfo, method);
        // 这个版本没有对Get和Post进行区分
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
                    return paramList;
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
                return param;
            });
            // 使用反射调用映射函数
            Param param = new Param(paramMap);
            Method actionMethod = handler.getActionMethod();
            Object result = ReflectionUtil.invokeMethod(bean, actionMethod, param);
            // 判断返回类型注解注解，返回视图, 该版本逻辑是注解和返回类型匹配才会返回成功, 为的是处理Json or View这种情况
            boolean isResponseViewPresent = actionMethod.isAnnotationPresent(ResponseView.class);
            boolean isResponseBodyPresent = actionMethod.isAnnotationPresent(ResponseBody.class);

            if (isResponseViewPresent && result instanceof View) {
                View view = (View) result;
                if (view.getPath().startsWith("/")) {
                    response.sendRedirect(request.getContextPath() + view.getPath());
                    return;
                }
                for (Map.Entry<String, Object> entry : view.getModel().entrySet()) {
                    request.setAttribute(entry.getKey(), entry.getValue());
                }
                request.getRequestDispatcher(ConfigHelper.getAppHTMLPath()+view.getPath()).forward(request, response);
            }
            // 返回Json数据
            if (isResponseBodyPresent && result instanceof Json) {
                Json json = (Json) result;
                PrintWriter writer = response.getWriter();
                writer.write(JsonUtil.parse(json));
                writer.flush();
                writer.close();
                return;
            }
            String annotationType = null;
            if (isResponseViewPresent) {
                annotationType = ResponseView.class.getName();
            }
            if (isResponseBodyPresent) {
                annotationType = ResponseBody.class.getName();
            }
            try {
                throw new ClassCastException("result class does not match the annotation type "+annotationType+"");
            } catch (ClassCastException e) {
                LOGGER.error(e.getMessage());
            }
        }
    }
}
