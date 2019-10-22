package com.ayang818.myspring.servlet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * @ClassName HelloServlet
 * @Dessription TODO
 * @Author 杨丰畅
 * @Date 2019/10/21 16:18
 **/

@javax.servlet.annotation.WebServlet("/hello")
public class HelloServlet extends javax.servlet.http.HttpServlet {
    @Override
    protected void doGet(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse res) throws ServletException, IOException {
        DateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = dateformat.format(System.currentTimeMillis());
        req.setAttribute("currentTime", currentTime);
        System.out.println("nono no");
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("/WEB-INF/jsp/hello.jsp");
        requestDispatcher.forward(req, res);
    }
}