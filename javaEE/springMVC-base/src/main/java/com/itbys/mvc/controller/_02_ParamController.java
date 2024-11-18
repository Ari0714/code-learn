package com.itbys.mvc.controller;

import com.itbys.mvc.controller.pojo.User;
import com.sun.xml.internal.ws.server.ServerRtException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Arrays;

/**
 * Author xx
 * Date 2023/8/12
 * Desc SpringMVC获取请求参数
 */
@Controller
public class _02_ParamController {

//    @RequestMapping("/")
//    public String test01() {
//        return "index_param";
//    }


    /**
     * 通过servlet API获取请求参数
     */
//    @RequestMapping("/target")
//    public String test02(HttpServletRequest httpServletRequest){
//        String usermame = httpServletRequest.getParameter("username");
//        String passwd = httpServletRequest.getParameter("passwd");
//        System.out.println("username="+usermame+", passwd="+passwd);
//        return "target";
//    }

    /**
     * 通过控制器的形参获取请求参数
     * 1、多个同名参数：String[] 获取数组，String获取以，分割
     * 2、@RequestParam参数
     *    value = "usermame",
     *    required = false,
     *    defaultValue = "xx"  请求参数==null 或 ''都生效
     * 3、@RequestHeader参数 同样有3个属性
     * 4、@CookieValue参数 同样有3个属性
     */
    //1、请求参数与形参同名
//    @RequestMapping("/target")
//    public String test02(String usermame, String passwd, String[] hobby){
//        System.out.println("username="+usermame+", passwd="+passwd+", hobby="+ Arrays.toString(hobby));
//        return "target";
//    }

    //1、请求参数与形参不同名
//    @RequestMapping("/target")
//    public String test02(@RequestParam(value = "usermame", required = false, defaultValue = "xx") String usermame,
//                         String passwd,
//                         String[] hobby,
//                         @RequestHeader(value = "Host") String host,
//                         HttpServletRequest httpServletRequest,
//                         @CookieValue(value = "JSESSIONID") String JSESSIONID) {
//        System.out.println("username=" + usermame + ", passwd=" + passwd + ", hobby=" + Arrays.toString(hobby));
//        System.out.println("host=" + host);
//        //cookie是客户端，session是服务端，session依赖cookie; 创建session才有cookie
//        HttpSession session = httpServletRequest.getSession();
//        System.out.println("JSESSIONID=" + JSESSIONID);
//        return "target";
//    }

    /**
     * 通过pojo获取请求参数
     */
//    @RequestMapping("/target")
//    public String test03(User user){
//        System.out.println(user.toString());
//        return "target";
//    }

}
