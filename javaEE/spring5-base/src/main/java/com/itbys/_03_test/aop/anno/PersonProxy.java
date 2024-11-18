package com.itbys._03_test.aop.anno;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * Author xx
 * Date 2022/7/31
 * Desc
 */
@Component
@Aspect  //开启代理对象
public class PersonProxy {

    /**
     * 环绕之前...
     * Before...
     * 午休...
     * After...
     * AfterThrowing （触发时后面2个不执行）
     * AfterReturning...
     * 环绕之后...
     */

    //公共切入点的提取
    @Pointcut(value = "execution(* com.itbys._03_test.aop.anno.Person.add(..))")
    public void commonPoint(){
    }

    @Before(value = "commonPoint()")
    public void before(){
        System.out.println("Before...");
    }

    //后置通知
    @After(value = "execution(* com.itbys._03_test.aop.anno.Person.add(..))")
    public void after(){
        System.out.println("After...");
    }

    //最终通知
    @AfterReturning(value = "execution(* com.itbys._03_test.aop.anno.Person.add(..))")
    public void afterReturning(){
        System.out.println("AfterReturning...");
    }

    //异常通知：add异常才通知，出发时候后置通知和环绕后不执行
    @AfterThrowing(value = "execution(* com.itbys._03_test.aop.anno.Person.add(..))")
    public void afterThrowing(){
        System.out.println("AfterThrowing...");
    }


    @Around(value = "execution(* com.itbys._03_test.aop.anno.Person.add(..))")
    public void around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        System.out.println("环绕之前...");
        proceedingJoinPoint.proceed();
        System.out.println("环绕之后...");

    }

}
