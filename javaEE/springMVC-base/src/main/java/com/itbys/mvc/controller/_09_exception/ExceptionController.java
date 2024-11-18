package com.itbys.mvc.controller._09_exception;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Author chenjie
 * Date 2023/10/11
 * Desc
 */
@Controller
public class ExceptionController {

    @RequestMapping(value = "/testException")
    public String test01(){
        System.out.println(1 / 0);
        return "_09_exception/success";
    }

}
