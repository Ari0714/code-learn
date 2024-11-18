package com.itbys.util;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.Charsets;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Base64;

/**
 * Author chenjie
 * Date 2023/10/26
 * Desc
 */
public class MyTestConn {

    static String uatURL = "https://bdp-uat.tcl.com/data-development/bdp/confConnect/testConnect";
    static String prodURL = "https://bdp.tcl.com/data-development/bdp/confConnect/testConnect";

    public static void main(String[] args) throws Exception {

        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("/opt/test_chenjie/ss.txt"));

        Connection conn = MyMySQLUtil.getConn();
        //创建命令执行对象
        Statement stmt = conn.createStatement();
        String sql = "SELECT * from  bdp_conf_connect";
        System.out.println(sql);

        //执行对象执行SQL语句
        ResultSet resultSet = stmt.executeQuery(sql);
        while (resultSet.next()) {
            //CREATE TABLE `bdp_conf_connect` (
            //  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号',
            //  `name` varchar(100) DEFAULT NULL COMMENT '连接名称',
//            3     //  `type_id` int(11) DEFAULT NULL COMMENT '连接类型',
            //  `request_type` int(11) DEFAULT '1' COMMENT '请求类型 1为GET,2为post ',
//            5    //  `host` varchar(100) DEFAULT NULL COMMENT '主机名',
            //  `kafka_url` varchar(100) DEFAULT NULL COMMENT '接口的地址',
            //  `interface_url` varchar(100) DEFAULT NULL COMMENT '接口的地址',
            //  `ftp_file_location` varchar(255) DEFAULT NULL COMMENT '用户设置的ftp上传文件地址',
//            9     //  `port` varchar(100) DEFAULT NULL COMMENT '端口',
            //  `username` varchar(100) DEFAULT NULL COMMENT '用户名称',
            //  `password` varchar(500) DEFAULT NULL COMMENT '密码',
            //  `dbname` varchar(100) DEFAULT NULL COMMENT '数据库名称',
            //  `code` int(11) DEFAULT '0' COMMENT '编码类型 ',
            //  `format` varchar(100) DEFAULT NULL COMMENT '格式 ',
            //  `topic` varchar(100) DEFAULT NULL COMMENT '会话 ',
            //  `delimit_id` int(11) DEFAULT '0' COMMENT '分隔符类型 ',
            //  `desc` varchar(200) DEFAULT NULL,
            //  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
            //  `create_per` int(11) DEFAULT NULL COMMENT '创建用户',
            //  `mod_time` datetime DEFAULT NULL COMMENT '修改时间',
            //  `mod_per` int(11) DEFAULT NULL COMMENT '修改用户',
            //  `web_service_url` varchar(500) DEFAULT NULL COMMENT 'webservice服务地址',
            //  `target_name_space` varchar(250) DEFAULT NULL COMMENT 'webservice命名空间',
            //  `function_name` varchar(100) DEFAULT NULL COMMENT 'webService调用的方法',
            //  `department_id` varchar(64) DEFAULT NULL COMMENT '部门唯一',
            //  `output_table_name` varchar(100) DEFAULT NULL COMMENT '输出表名',
            //  `connect_status` smallint(6) DEFAULT NULL COMMENT '连接状态: 1:正常 2:失败',
            //  `connect_enable` smallint(6) DEFAULT NULL COMMENT '是否启用 0:不启用 1:启用',
            //  `role_id` bigint(20) DEFAULT NULL COMMENT '授权的角色id',
            //  `metastore_uris` varchar(255) DEFAULT NULL COMMENT 'metastoreUris --hive数据源',
            //  `default_fs` varchar(255) DEFAULT NULL COMMENT 'defaultFs --hive数据源',
            //  `namespace` varchar(255) DEFAULT NULL COMMENT 'hive 数据源 namespace',
            //  `ha_model` int(1) DEFAULT '1' COMMENT '1 HA  2 非HA',
            //  `name_node_port` int(5) DEFAULT NULL COMMENT 'hive 数据源 node端口',
            //  `name_node` varchar(255) DEFAULT NULL COMMENT 'hive 数据源 name_node',
            //  `cron_queue` varchar(255) DEFAULT NULL COMMENT 'hive类型数据源的调度队列',
            //  `oracle_nts_url` longtext DEFAULT NULL COMMENT 'oracle集群模式的url',
            //  `department_type` int(1) DEFAULT '0' COMMENT '部门的类型 0：机构部门（内部部门）  1：行政部门（tcl外部部门）',
            //  `options` varchar(255) DEFAULT NULL,
            //  `tenant_id` int(11) DEFAULT NULL COMMENT '租户id',
            //  `auth_mech` int(1) DEFAULT NULL COMMENT ' impala认证方式：0：没有认证 ，1：使用Kerberos认证（暂时不做） 2： 使用用户名来认证，3：使用用户名和密码认证',
            //  `load_url` varchar(255) DEFAULT NULL COMMENT 'starRocks 数据源的loadUrl(fe地址)',
            //  `token_request_type` int(11) DEFAULT NULL COMMENT '动态密钥请求类型 1为GET,2为post  3.post-raw',
            //  `token_url` varchar(255) DEFAULT NULL COMMENT '动态秘钥接口url',
            //  `variable_name` varchar(255) DEFAULT NULL COMMENT '动态引用参数变量名',
            //  `token_file_location` varchar(255) DEFAULT NULL COMMENT 'token取值路径',
            //  `enums_value` varchar(50) DEFAULT NULL COMMENT '特殊处理类型 token：(动态引用令牌) variable:(动态引用变量) ',
            //  PRIMARY KEY (`id`) /*T![clustered_index] CLUSTERED */
            //) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin AUTO_INCREMENT=2160867 COMMENT='新增数据源连接表'

            String id = resultSet.getString(1);
            String name = resultSet.getString(2);
            String typeId = resultSet.getString(3);
            String host = resultSet.getString(5);
            String port = resultSet.getString(9);
            String username = resultSet.getString(10);
//            String password = resultSet.getString(11);
            String dbname = resultSet.getString(12);
            String desc = resultSet.getString(17);

            System.out.println("\n" + name + " && " + host + ":" + port + " && " + typeId);

            String s = sendMsg2(id, name, typeId, host, port, username, dbname, desc, args[0]);
            System.out.println(s);

            bufferedWriter.write("\n" + name + " && " + host + ":" + port + " && " + typeId);
            bufferedWriter.write("\n" + s + "\n");
            bufferedWriter.flush();


        }
        //关闭资源
        stmt.close();
        conn.close();

        bufferedWriter.close();


    }


    public static String sendMsg2(String id, String name, String typeId, String host,
                                  String port, String username, String dbname, String desc, String token) {
//        log.info("发送T信内容：contentType=" + contentType + "->" + content);


        //{
        //	"name": "电子_CK_QMS数据中心",
        //	"typeId": 15,
        //	"host": "10.74.141.149",
        //	"port": "8123",
        //	"username": "data_sit_rw",
        //	"authMech": null,
        //	"password": null,
        //	"dbname": null,
        //	"ftpFileLocation": null,
        //	"format": null,
        //	"desc": "qms项目数据服务，联系人苏军",
        //	"id": 1950867,
        //	"oracleNtsUrl": null,
        //	"options": null,
        //	"loadUrl": null,
        //	"roleId": null,
        //	"departmentId": null
        //}

        String timeStr = MyDateUtil.getTS() + "";
        HttpRequest request = HttpUtil.createRequest(Method.POST, prodURL);
        request.header("Content-Type", "application/json");
        request.header("timestamp", timeStr);
        request.header("token", token);

        JSONObject param = new JSONObject();
        param.put("id", id);
        param.put("name", name);
        param.put("typeId", typeId);
        param.put("host", host);
        param.put("port", port);
        param.put("username", username);
        param.put("authMech", null);
        param.put("password", null);
        param.put("dbname", dbname);
        param.put("ftpFileLocation", null);
        param.put("format", null);
        param.put("desc", desc);
        param.put("oracleNtsUrl", null);
        param.put("options", null);
        param.put("loadUrl", null);
        param.put("roleId", null);
        param.put("departmentId", null);

        request.body(param.toJSONString());

//        log.info("调用T信发送消息接口-请求头:" + JSON.toJSONString(request.headers()));
//        log.info("调用T信发送消息接口-请求参数:" + param.toJSONString());

        String body = request.execute().body();
        return body;
//        log.info("调用T信发送消息接口返回：" + body);
//        System.out.println(body);
//        return !"0".equals(JSONPath.extract(body, "$.code")) ? (String) JSONPath.extract(body, "$.msg") : null;
    }


//    private static String decryptPwd(String password) {
////        if (StringUtils.isNotEmpty(confConnect.getPassword())) {
////            String password = confConnect.getPassword();
////            log.info("解密前的密文={}", password);
//            if (password.length() >= 32) {//前端传的是密文
//                String pwd = decrypt(password,
//                        "1234567890ABCDEF1234567890ABCDEf");
////                log.info("publicKey={},解密前的密文={},解密后的密文:={}", systemCenterConfig.getPublicKey(), password, pwd);
////                if (null == pwd) {
////                    return "前后端秘钥加密解密不对称";
////                }
//
//                return pwd;
////                confConnect.setPassword(pwd);
//            }
////        }
//        return null;
//    }


//    public static String decrypt(String source, String key) {
//        try {
//            byte[] bytes = key.getBytes(StandardCharsets.UTF_8);
//            return new String(decrypt(source, bytes));
//        } catch (Exception var3) {
//            var3.printStackTrace();
//            return null;
//        }
//    }

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


