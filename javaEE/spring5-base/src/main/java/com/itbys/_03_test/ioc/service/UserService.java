package com.itbys._03_test.ioc.service;

import com.itbys._03_test.ioc.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Author xx
 * Date 2023/8/9
 * Desc
 */
@Service
public class UserService {

    @Autowired
    public UserDao userDao;

    public void add(){
       userDao.add();
    }



}
