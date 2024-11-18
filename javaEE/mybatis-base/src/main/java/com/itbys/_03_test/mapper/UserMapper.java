package com.itbys._03_test.mapper;


import com.itbys._03_test.entity.User;

import java.util.List;

public interface UserMapper {

    /**
     * 添加用户信息
     */
    int insertUser();

    /**
     * 修改用户信息
     */
    void updateUser();

    /**
     * 删除用户信息
     */
    void deleteUser();

    /**
     * 查询用户信息
     */
    User getUserById();
    List<User> getAllUser();

    /**
     * 查询用户信息（获取参数值）
     */
    User getUserByName(String username);

}
