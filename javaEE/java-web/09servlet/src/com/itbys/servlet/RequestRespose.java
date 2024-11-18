package com.itbys.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

public class RequestRespose extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //资源路径
        String requestURI = request.getRequestURI();

        //uri
        String remoteHost = request.getRemoteHost();

        //请求头
        String header = request.getHeader("User-Agent");

        //method
        String method = request.getMethod();


        //获取
        request.setCharacterEncoding("utf-8");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String[] hobby = request.getParameterValues("hobby");  //数组不能直接打印  list可以

        System.out.println(username + "," + password + "," + Arrays.asList(hobby));

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


    }
}
