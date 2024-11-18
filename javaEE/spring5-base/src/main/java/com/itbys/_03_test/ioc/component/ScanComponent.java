package com.itbys._03_test.ioc.component;

import org.springframework.stereotype.Component;

/**
 * Author xx
 * Date 2023/8/9
 * Desc
 */
@Component(value = "scanComponent")   // //默认类名首字母小写
public class ScanComponent {

    public void dog(){
        System.out.println("汪汪汪。。。");
    }

}
