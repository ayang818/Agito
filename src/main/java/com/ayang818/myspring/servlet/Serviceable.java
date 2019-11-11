package com.ayang818.myspring.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface Serviceable {
    Object mappingAndInvoke(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
