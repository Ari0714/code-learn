package com.itbys._03_test.ioc;

import com.alibaba.druid.pool.DruidDataSource;
import com.itbys._03_test.ioc.bean.Emp;
import com.itbys._03_test.ioc.bean.User;
import com.itbys._03_test.ioc.bean.collection.Book;
import com.itbys._03_test.ioc.bean.collection.Stu;
//import com.itbys.test.ioc.config.SpringConfig;
//import com.itbys.test.ioc.dao.UserDaoImpl;
//import com.itbys.test.ioc.dao.UserDaoImpl03;
import com.itbys._03_test.ioc.config.SpringConfig;
import com.itbys._03_test.ioc.service.UserService;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Author xx
 * Date 2022/7/31
 * Desc
 */
public class Spring5Test {


    @Test
    public void test01() {

        ApplicationContext context = new ClassPathXmlApplicationContext("bean1.xml");
        User user = context.getBean("user", User.class);

        System.out.println(user);
    }

    @Test
    public void test02() {

        ApplicationContext context = new ClassPathXmlApplicationContext("bean2.xml");
        UserService service = context.getBean("service", UserService.class);
        service.add();
    }

    @Test
    public void test03() {

        ApplicationContext context = new ClassPathXmlApplicationContext("bean3.xml");
        Emp emp = context.getBean("emp", Emp.class);

        System.out.println(emp);
    }

    @Test
    public void test04() {

        ApplicationContext context = new ClassPathXmlApplicationContext("bean4.xml");
        Stu stu = context.getBean("stu", Stu.class);

        System.out.println(stu);
    }

    //数组
    @Test
    public void test05() {

        ApplicationContext context = new ClassPathXmlApplicationContext("bean5.xml");
        Book book = context.getBean("book", Book.class);

        System.out.println(book);
    }


    // <!--两种bean   单实例singleton、多实例prototype-->
    @Test
    public void test06() {

        ApplicationContext context = new ClassPathXmlApplicationContext("bean6.xml");
        User user = context.getBean("emp", User.class);
        User user02 = context.getBean("emp", User.class);

        System.out.println(user.hashCode());
        System.out.println(user02.hashCode());
    }

    //<!--自动装配 byName、byType-->
    @Test
    public void test07() {

        ApplicationContext context = new ClassPathXmlApplicationContext("bean7.xml");
        Emp emp = context.getBean("emp", Emp.class);

        System.out.println(emp);
    }

    //连接池、引入外部配置文件
    @Test
    public void test08() {

        ApplicationContext context = new ClassPathXmlApplicationContext("bean8.xml");
        DruidDataSource dataSource = context.getBean("dataSource", DruidDataSource.class);
        System.out.println(dataSource.getUsername());
    }


    //bean的注解方式  //！！！只能有一个配置文件（xml或@Configuration作为配置类替换xml）
//    @Test
//    public void test09() {
//        ApplicationContext context = new ClassPathXmlApplicationContext("bean9.xml");
//        ScanComponentSpringConfig springConfig = context.getBean("scanComponentSpringConfig", ScanComponentSpringConfig.class);
//        springConfig.cat();
//    }

    // @Configuration  //作为配置类，替换xml配置文件
//    @Test
//    public void test10() {
//        ApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);
//        ScanComponentSpringConfig springConfig = context.getBean("scanComponentSpringConfig", ScanComponentSpringConfig.class);
//        springConfig.cat();
//    }


    // 属性注入
    @Test
    public void test11() {
        ApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);
        UserService userService = context.getBean("userService", UserService.class);
        userService.add();
    }


}
