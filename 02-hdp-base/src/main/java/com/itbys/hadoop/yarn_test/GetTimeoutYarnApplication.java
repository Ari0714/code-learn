package com.itbys.hadoop.yarn_test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Author Ari
 * Date 2023/9/18
 * Desc 获取超时任务
 * 1、TEZ任务
 * 2、持续时间大于1h
 */
public class GetTimeoutYarnApplication {

    public static void main(String[] args) {

        //application_1657791904297_25970、application_1657791904297_25622
        String applicationId = args[0];

        String returnStr = execCmd(new String[]{"/bin/sh", "-c", "curl -X GET -H \"Accept:application/json\" \"http://hdp103:8088/ws/v1/cluster/apps/" + applicationId + "\""});
        JSONObject jsonObject = JSON.parseObject(returnStr);

        JSONObject appJson = jsonObject.getJSONObject("app");
        String applicationType = appJson.getString("applicationType");
        Long elapsedTime = appJson.getLong("elapsedTime") / 1000;

        if ("TEZ".equals(applicationType) && elapsedTime > 3600) {
            System.out.println(applicationId);
        }

    }


    /**
     * @desc 执行curl命令
     * @param: command
     * @return: java.lang.String
     */
    public static String execCmd(String[] command) {
        StringBuilder output = new StringBuilder();
        Process p;
        try {
            p = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return output.toString();
    }

}
