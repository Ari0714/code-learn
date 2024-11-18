package com.itbys.servlet;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class ServletHello implements Servlet {
    @Override
    public void init(ServletConfig servletConfig) throws ServletException {

        System.out.println("getServletName:" + servletConfig.getServletName());
        System.out.println("getInitParameter:" + servletConfig.getInitParameter("url"));
        System.out.println("getServletContext" + servletConfig.getServletContext());

    }

    @Override
    public ServletConfig getServletConfig() {
        return null;
    }

    @Override
    public void service(ServletRequest servletrequest, ServletResponse servletResponse) throws ServletException, IOException {

//        System.out.println("hello servlet");

        HttpServletRequest httpServletRequest = (HttpServletRequest) servletrequest;

        String method = httpServletRequest.getMethod();
        if ("POST".equals(method))
            System.out.println("post");
        else if ("GET".equals(method))
            System.out.println("get");

    }

    @Override
    public String getServletInfo() {
        return null;
    }

    @Override
    public void destroy() {

    }
}
