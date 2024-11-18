package com.itbys.mvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

/**
 * Author xx
 * Date 2023/8/22
 * Desc 视图
 */
@Controller
public class _04_View {

    /**
     * thymeleaf view
     *  1、ThymeleafView  去点前缀后缀，通过ThymeleafViewResolver解析
     *  2、转发视图：是InternalResourceView，
     *  3、重定向：RedirectView
     *  4、区别
     *    1、转发，浏览器发送一次请求，服务器发送两次；能获取请求域数据；能访问WEB-INF的资源；不能跨域（只能访问WEB-INF）
     *    2、重定向：浏览器发送二次请求；不能获取请求域数据；不能访问WEB-INF的资源；能跨域
     *  5、view-controller
     */
//    @RequestMapping(value = "/", method = RequestMethod.GET)
//    public String test01() {
//        return "index_view";
//    }

    //转发
//    @RequestMapping("/target")
//    public String test02(){
//        return "forward:/target_view";
//    }
//    @RequestMapping("/target_view")
//    public String test03(){
//        return "target_view.html";
//    }

    //转发
//    @RequestMapping("/target")
//    public String test03(){
//        return "redirect:/redirect_target_view";
//    }
//    @RequestMapping("/redirect_target_view")
//    public String test04(){
//        return "target_view_redirect.html";
//    }


}
