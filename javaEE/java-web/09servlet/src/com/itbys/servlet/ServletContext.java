package com.itbys.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ServletContext extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        javax.servlet.ServletContext context = getServletConfig().getServletContext();

        //获取context param
        String initParameter = context.getInitParameter("");

        //工程路径 相对
        String contextPath = context.getContextPath();

        //工程路径 绝对
        String realPath = context.getRealPath("/");


        //存取数据
        context.setAttribute("key1", "value1");

        Object key1 = context.getAttribute("key1");


    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


    }
}
