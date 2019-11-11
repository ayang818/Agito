package com.ayang818.myspring.servlet;

import com.ayang818.myspring.helper.ControllerHelper;
import com.ayang818.myspring.helper.InitHelper;
import com.ayang818.myspring.util.Handler;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DispatchServlet extends HttpServlet {
    @Override
    public void init() {
        InitHelper.init();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        String pathInfo = request.getPathInfo();
        String method = request.getMethod().toLowerCase();
        Handler handler = ControllerHelper.getHandler(pathInfo, method);

    }

    @Override
    protected  void doPost(HttpServletRequest request, HttpServletResponse response) {

    }
}
