package com.itbys.mvc.controller._08_interceptor;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * Author chenjie
 * Date 2023/10/10
 * Desc
 */
@Controller
public class InterceptorController {

    /**
     *  多个拦截器
     *  1、prehandler按照配置顺序执行、posthandler、aftercompletion反序执行
     *  2、若某个拦截器的preHandle()返回了false，preHandle()返回false和它之前的拦截器的preHandle()都会执行，postHandle()都不执行，返回false
     * 的拦截器之前的拦截器的afterComplation()会执行
     */
    @RequestMapping(value = "/testInterceptor")
    public String test01(){
        return "_08_interceptor/error.html";
    }


}
