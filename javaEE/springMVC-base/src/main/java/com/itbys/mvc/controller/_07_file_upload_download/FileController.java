package com.itbys.mvc.controller._07_file_upload_download;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * Author chenjie
 * Date 2023/10/10
 * Desc
 */
@Controller
public class FileController {

    //文件下载
    @RequestMapping(value = "/testDownload", method = RequestMethod.GET)
    public ResponseEntity<byte[]> test01(HttpSession session) throws IOException {
        //获取ServletContext对象
        ServletContext servletContext = session.getServletContext();
        //获取服务器中文件的真实路径
        String realPath = servletContext.getRealPath("/statics/img/1.jpg");
        //创建输入流
        InputStream is = new FileInputStream(realPath);
        //创建字节数组
        byte[] bytes = new byte[is.available()];
        //将流读到字节数组中
        is.read(bytes);
        //创建HttpHeaders对象设置响应头信息
        MultiValueMap<String, String> headers = new HttpHeaders();
        //设置要下载方式以及下载文件的名字
        headers.add("Content-Disposition", "attachment;filename=1.jpg");
        //设置响应状态码
        HttpStatus statusCode = HttpStatus.OK;
        //创建ResponseEntity对象
        ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(bytes, headers, statusCode);
        //关闭输入流
        is.close();
        return responseEntity;
    }


    //2、文件上传
//    文件上传要求form表单的请求方式必须为post，并且添加属性enctype="multipart/form-data"
//    SpringMVC中将上传的文件封装到MultipartFile对象中，通过此对象可以获取文件相关信息
//    上传步骤：
//    a>添加依赖：
//    b>在SpringMVC的配置文件中添加配置：
//    c>控制器方法：
    @RequestMapping("/testUpload")
    public String testUp(MultipartFile photo, HttpSession session) throws IOException {
        //获取上传的文件的文件名, input的name属性
        String fileName = photo.getOriginalFilename();
        System.out.println(fileName);
//        String name = photo.getName();
//        System.out.println(name);

        //处理文件重名问题
        String hzName = fileName.substring(fileName.lastIndexOf("."));
        fileName = UUID.randomUUID().toString() + hzName;
        //获取服务器中photo目录的路径
        ServletContext servletContext = session.getServletContext();
        String photoPath = servletContext.getRealPath("/photo");  //一定使用 ‘/’ ，不然为null
        System.out.println(photoPath);
        File file = new File(photoPath);
        if (!file.exists()) {
            file.mkdir();
        }
        String finalPath = photoPath + File.separator + fileName;

        //实现上传功能
        photo.transferTo(new File(finalPath));

        return "_07_file_upload_download/success";
    }

}
