package com.itbys.mvc.controller._06_HttpMessageConverter;

import com.itbys.mvc.controller.pojo.User;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Author chenjie
 * Date 2023/10/10
 * Desc
 */
@Controller
public class HttpMessageConverterController {

    /**
     * 获得请求体
     * requestBody、RequestEntity
     * 复合注解: RestController = ResponseBody + Controller
     */
//    @RequestMapping(value = "/addMessage", method = RequestMethod.POST)
//    public String test01(@RequestBody String requestBody) {
//        System.out.println("requestBody: " + requestBody);
//        return "_06_HttpMessageConverter/success";
//    }

//    @RequestMapping(value = "/addMessage", method = RequestMethod.POST)
//    public String test02(RequestEntity<String> requestEntity) {
//        System.out.println("requestEntity.getHeaders: " + requestEntity.getHeaders());
//        System.out.println("requestEntity.getBody: " + requestEntity.getBody());
//        return "_06_HttpMessageConverter/success";
//    }

    /**
     * 响应体: 直接返回内容
     * ResponseBody
     */
    //字符串
//    @RequestMapping(value = "/addMessage", method = RequestMethod.POST)
//    @ResponseBody
//    public String test03(RequestEntity<String> requestEntity) {
//        return "_06_HttpMessageConverter/success";
//    }

    //对象: 需要jackson依赖
    @RequestMapping(value = "/addMessage", method = RequestMethod.POST)
    @ResponseBody
    public User test04(RequestEntity<String> requestEntity) {
        return new User("coco", "111111", "tennis");
    }

}
