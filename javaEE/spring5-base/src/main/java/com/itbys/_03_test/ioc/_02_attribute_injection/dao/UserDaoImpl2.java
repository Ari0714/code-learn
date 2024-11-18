package com.itbys._03_test.ioc._02_attribute_injection.dao;

import org.springframework.stereotype.Repository;

/**
 * Author xx
 * Date 2022/7/31
 * Desc
 */
@Repository
public class UserDaoImpl2 implements UserDao2 {
    @Override
    public void add() {
        System.out.println("dao add...");
    }
}
