package com.itbys._02_user_behaviors_analysis.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApacheLog {

    //91.177.205.119 - - 17/05/2015:10:05:34 +0000 GET /reset.css
    //case class ApacheLog (ip:String,userId:String,timeStamp:Long,method:String,url:String)
    //case class LogWinCount(url:String,windowEnd:Long,count:Long)
    private String ip;
    private String userId;
    private Long timeStamp;
    private String method;
    private String url;

}

