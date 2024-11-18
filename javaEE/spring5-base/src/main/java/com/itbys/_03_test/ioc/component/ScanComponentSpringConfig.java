package com.itbys._03_test.ioc.component;

import org.springframework.stereotype.Component;

/**
 * Author xx
 * Date 2023/8/9
 * Desc
 */
@Component(value = "scanComponentSpringConfig")   // //默认类名首字母小写
public class ScanComponentSpringConfig {

    public void cat() {
        System.out.println("喵喵喵。。。");
    }

}
