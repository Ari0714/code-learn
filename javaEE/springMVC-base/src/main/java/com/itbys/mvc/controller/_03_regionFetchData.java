package com.itbys.mvc.controller;

import com.itbys.mvc.controller.pojo.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * Author xx
 * Date 2023/8/14
 * Desc 域对象获取数据
 */
@Controller
public class _03_regionFetchData {

//    @RequestMapping("/")
//    public String test01() {
//        return "index_region";
//    }


    /**
     * 使用servlet向request域对象共享数据
     */
//    @RequestMapping("/target")
//    public String test02(HttpServletRequest httpServletRequest){
//        httpServletRequest.setAttribute("username","hello, servlet");
//        return "target";
//    }

    /**
     * 使用ModelAndView向request域对象共享数据
     * 1、Model、ModelMap、Map类型的参数其实本质上都是 BindingAwareModelMap 类型的
     * 2、统一返回modelview方法
     */
    //1、使用ModelAndView
//    @RequestMapping("/target")
//    public ModelAndView test02(){
//
//        /**
//         * ModelAndView有model和view功能
//         * model向请求域共享数据
//         * view设置视图，实现页面跳转
//         */
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.addObject("username","hello, ModelAndView");
//        modelAndView.setViewName("target");
//
//        return modelAndView;
//    }

    //2、使用Model
//    @RequestMapping("/target")
//    public String test03(Model model){
//        model.addAttribute("username","hello, Model");
//        return "target";
//    }

    //3、使用map
//    @RequestMapping("/target")
//    public String test04(Map<String,Object> map){
//        map.put("username","hello, map");
//        return "target";
//    }

    //4、使用modelMap
//    @RequestMapping("/target")
//    public String test05(ModelMap map){
//        map.put("username","hello, ModelMap");
//        return "target";
//    }


    /**
     * 使用servlet向session域对象共享数据
     */
//    @RequestMapping("/target")
//    public String test06(HttpSession httpSession){
//        httpSession.setAttribute("username","hello session");
//        return "target";
//    }


    /**
     * 使用servlet向application对象共享数据
     */
//    @RequestMapping("/target")
//    public String test07(HttpSession httpSession){
//        ServletContext application = httpSession.getServletContext();
//        application.setAttribute("username","hello application");
//        return "target";
//    }

}
