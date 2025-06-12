package com.itbys.util;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import org.apache.commons.codec.Charsets;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * Author Ari
 * Date 2023/10/26
 * Desc
 */
public class MyRobotHttpUtil {

    static String prodURL = "https://teamwork.getech.cn/athena/http/web/api/msg/push/callback";
    static String preURL = "https://mtest.getech.cn/athena/http/web/api/msg/push/callback";

    public static void main(String[] args) throws Exception {

        //正式
        sendMsg("ai-90110", "Vew2Hcce", "955da14303d8f0b275993ff8586815b0",
                "ai-g@10000737847", "测试推送 by chenjie，哈哈哈。--" + MyDateUtil.getYYYYMMDD_HHMMSS(), 0);

        //测试
//        sendMsg("https://mtest.getech.cn/athena/http/web/api/msg/push/callback",
//                "jPTRWQFE", "cd4a0ca33287195d4f4e2526cc3d09e1",
//                "ai-g@10000219558", "测试推送 by chenjie", 0);

    }



    public static String sendMsg(String robotId, String appId, String appSecret, String sessionId, String content, int contentType) {
//        log.info("发送T信内容：contentType=" + contentType + "->" + content);
        String timeStr = MyDateUtil.getTS() + "";
        HttpRequest request = HttpUtil.createRequest(Method.POST, prodURL);
        request.header("Content-Type", "application/json");
        request.header("appId", appId);
        request.header("timestamp", timeStr);
        request.header("sign", sign(appSecret + timeStr));

        JSONObject param = new JSONObject();
        param.put("appId", "1");
        param.put("sender", robotId);
        param.put("sessionId", sessionId);
        param.put("content", content);
        param.put("contentType", contentType);
        param.put("sessionType", "ai");
        request.body(param.toJSONString());

//        log.info("调用T信发送消息接口-请求头:" + JSON.toJSONString(request.headers()));
//        log.info("调用T信发送消息接口-请求参数:" + param.toJSONString());

        String body = request.execute().body();
//        log.info("调用T信发送消息接口返回：" + body);
        System.out.println(body);
        return !"0".equals(JSONPath.extract(body, "$.code")) ? (String) JSONPath.extract(body, "$.msg") : null;
    }


    /**
     * 获取sign签名值工具类
     * 使用方式：直接调用sign(str)方法
     */
    /**
     * @return java.lang.String
     * @Description 获取sign值
     * @Param [strSrc]：参数值是由分配的密钥字符串拼接当前时间戳字符串（格式(yyyyMMddHHmmss)）所得
     */
    public static String sign(String strSrc) {
        // 进行Base64编码
        return Base64.getEncoder().encodeToString(encrypt(strSrc).getBytes(Charsets.UTF_8));
    }

    /**
     * Md5加密
     */
    public static String encrypt(String strSrc) {
        String strDes = null;
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
            byte[] bt = strSrc.getBytes();
            md.update(bt);
            strDes = bytes2Hex(md.digest());
        } catch (NoSuchAlgorithmException e) {
        }
        return strDes;
    }

    private static String bytes2Hex(byte[] bts) {
        String des = "";
        String tmp = null;
        for (int i = 0; i < bts.length; i++) {
            tmp = (Integer.toHexString(bts[i] & 0xFF));
            if (tmp.length() == 1) {
                des += "0";
            }
            des += tmp;
        }
        return des;
    }


}


