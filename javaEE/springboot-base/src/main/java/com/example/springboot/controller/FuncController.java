package com.example.springboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Author xx
 * Date 2024/1/4
 * Desc
 */
@Controller
@ResponseBody
public class FuncController {

    @Autowired
    RemoteShell remoteShell;

    @PostMapping({"/do-fileUpload"})
    public String do_index(String file1, String file2) throws IOException, InterruptedException, URISyntaxException {
//        System.out.println(file1 + ":" + file2);
//        String file1Name = "/HDFS/mydir/file1.txt";
//        String file2Name = "/HDFS/mydir/file2.txt";
        remoteShell.execShell("sh /opt/project/hdfs.sh");
//        System.out.println("==============================");
        return "file upload";
    }


    @RequestMapping({"/do-flume"})
    public String flume() {
        String command = "";
        remoteShell.execShell("sh /opt/project/flume.sh");
        return "flume";
    }


    @RequestMapping({"/do-wordCount"})
    public String wordCount() {
        String command = "";
        System.out.println("执行成功");
        remoteShell.execShell("sh /opt/project/mr.sh");
        return "mapreduce";

    }


}


