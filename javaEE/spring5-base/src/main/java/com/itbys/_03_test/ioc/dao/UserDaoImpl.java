package com.itbys._03_test.ioc.dao;

import org.springframework.stereotype.Repository;

/**
 * Author xx
 * Date 2022/7/31
 * Desc
 */
@Repository  //@Repository(value = "userDaoImpl")
public class UserDaoImpl implements UserDao {
    @Override
    public void add() {
        System.out.println("dao add...");
    }
}
