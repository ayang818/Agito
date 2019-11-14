package com.ayang818.agito.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface Serviceable {
    void mappingAndInvoke(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException;
}
