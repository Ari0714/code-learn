package com.itbys._03_test.ioc.bean.factory;

import com.itbys._03_test.ioc.bean.User;
import org.springframework.beans.factory.FactoryBean;

/**
 * Author xx
 * Date 2022/7/31
 * Desc
 */
public class Mybean implements FactoryBean {

    @Override
    public Object getObject() throws Exception {
        return new User("01","xx02","22");
    }

    @Override
    public Class<?> getObjectType() {
        return null;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}
