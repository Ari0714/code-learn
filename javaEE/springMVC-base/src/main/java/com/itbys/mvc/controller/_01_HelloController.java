package com.itbys.mvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Author xx
 * Date 2022/10/2
 * Desc
 */
@Controller
public class _01_HelloController {

//    @RequestMapping("/")
//    public String index() {
//        // 返回视图名称
//        return "index";
//    }

//    @RequestMapping("/target")
//    public String target() {
//        return "target";
//    }

    /**
     * value属性,必配置
     */
    //两个地址都可以跳转
//    @RequestMapping(value = {"/target","/target/hello"})
//    public String test02(){
//     return "target";
//    }


    /**
     * method属性, get、post、put、delete
     *  1、不设置post、get都支持访问
     *  2、衍生注解
     *     @GetMapping
     *     @PostMapping
     *     @PutMapping
     *     @DeleteMapping
     */
    // 使用method属性
//    @RequestMapping(value = "/target",method = RequestMethod.GET)
//    public String test03(){
//        return "target";
//    }

    //衍生注解
//    @GetMapping(value = "/target")
//    public String test03(){
//        return "target";
//    }


    /**
     * param属性, 根据请求参数匹配
     *
     * "param"：要求请求映射所匹配的请求必须携带param请求参数
     * "!param"：要求请求映射所匹配的请求必须不能携带param请求参数
     * "param=value"：要求请求映射所匹配的请求必须携带param请求参数且param=value
     * "param!=value"：要求请求映射所匹配的请求必须携带param请求参数但是param!=value
     */
//    @RequestMapping(value = "/target",params = {"username"})
//    public String test04(){
//        return "target";
//    }


    /**
     * headers属性, 请求头匹配
     *
     *  header"：要求请求映射所匹配的请求必须携带header请求头信息
     * "!header"：要求请求映射所匹配的请求必须不能携带header请求头信息
     * "header=value"：要求请求映射所匹配的请求必须携带header请求头信息且header=value
     * "header!=value"：要求请求映射所匹配的请求必须携带header请求头信息且header!=value
     */
//    @RequestMapping(value = "/target", headers = {"Host=localhost:9100"})
//    public String test05(){
//        return "target";
//    }


    /**
     * ant风格路径
     * ？：表示任意的单个字符
     * *：表示任意的0个或多个字符
     * **：表示任意的一层或多层目录
     */
    //注意：在使用**时，只能使用/**/xxx的方式
//    @RequestMapping(value = "/target/a?c")
//    public String test06(){
//        return "target";
//    }


    /**
     * springMVC路径的占位符
     * 1、原始方式：/deleteUser？id=1
     * 2、rest方式：/deleteUser/1
     */
//    @RequestMapping(value = "target/{id}/{username}")
//    public String test07(@PathVariable("id") String id, @PathVariable("username") String username) {
//        System.out.println("id=" + id + ", username=" + username);
//        return "target";
//    }


}
