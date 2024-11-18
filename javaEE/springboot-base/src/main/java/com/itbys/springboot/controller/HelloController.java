package com.itbys.springboot.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.Arrays;
import java.util.Map;

@Controller
public class HelloController {

    @RequestMapping({"/","/index.html"})
    public String index(){
        return "login2";
    }

    @ResponseBody
    @RequestMapping("/hello")
    public String hello() {
        return "hello,world ！！！";
    }

    @RequestMapping("/success")
    public String success(Map<String, Object> map) {
        map.put("hello", "你好");
        map.put("users", Arrays.asList("小狗，老狗"));
        map.put("hello_t", "<h1>你好吗？<h1>");
        return "success";
    }
}
