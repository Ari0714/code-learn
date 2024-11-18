package com.itbys.mvc.controller;

import com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Author xx
 * Date 2023/8/22
 * Desc restful
 */
@Controller
public class _05_restful {

    /**
     * 使用restful风格实现用户的增删改查
     * 1、查全量 get，/users
     * 2、根据id查询 get，/user/1
     * 3、删除 delete，/deleteUser/1
     * 4、更新 put， /updateUser/1
     * 5、添加 post， /addUser
     */
//    @RequestMapping("/")
//    public String test01() {
//        return "_05_restful/index";
//    }

    //查全量
//    @RequestMapping("/users")
//    public String test02() {
//        return "_05_restful/index_users.html";
//    }

    //根据id查询
//    @RequestMapping("/get_user/{id}")
//    public String test03() {
//        return "_05_restful/index_get_user.html";
//    }

    //添加用户
//    @RequestMapping(value = "/add_user", method = RequestMethod.POST)
//    public String test04(String username, String password) {
//        System.out.println("username: " + username + ", password: " + password);
//        return "_05_restful/index_add_user";
//    }

    //更新put：《put请求方式：form表单为post；2、隐藏域增加value=PUT》
//    @RequestMapping(value = "/update_user", method = RequestMethod.PUT)
//    public String test05(String username, String password){
//        System.out.println("username: " + username + ", password: " + password);
//        return "_05_restful/index_update_user";
//    }

    //删除delete：《put请求方式：form表单为post；2、隐藏域增加value=DELETE》

}
