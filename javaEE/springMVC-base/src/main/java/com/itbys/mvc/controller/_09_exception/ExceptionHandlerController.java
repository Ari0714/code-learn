package com.itbys.mvc.controller._09_exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Author chenjie
 * Date 2023/10/11
 * Desc
 */
@ControllerAdvice
public class ExceptionHandlerController {

    /**
     * 基于注解的异常处理
     */
    //@ExceptionHandler用于设置所标识方法处理的异常
    @ExceptionHandler(ArithmeticException.class)
    public String test01(Model model, Exception ex) {
        model.addAttribute("ex", ex);
        return "_09_exception/error";
    }


}
