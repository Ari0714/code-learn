package com.itbys._03_test.ioc._02_attribute_injection;

import com.itbys._03_test.ioc._02_attribute_injection.dao.UserDao2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Author xx
 * Date 2023/8/9
 * Desc
 */
@Service
public class UserService2 {


    /**
     * @Autowired 根据类型找接口实现类
     * @Qualifier(value = "userDaoImpl2")  与Autowired一起使用，根据名称找接口实现类，（当一个接口有多个实现类时）
     * @Resource(name = "userDaoImpl2")  不加name类型注入，加name名称注入。 ！！！javax.annotation.Resource包
     * @Value(value = "abc")  普通属性注入
     */
    @Autowired
//    @Qualifier(value = "userDaoImpl2")
//    @Resource(name = "userDaoImpl2")
    public UserDao2 userDao2;

    @Value(value = "abc")
    public String s;

    public void add(){
       userDao2.add();
        System.out.println("service addd." + s);
    }



}
