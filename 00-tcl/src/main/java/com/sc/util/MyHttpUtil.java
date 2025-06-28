package com.sc.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Author Ari
 * Date 2023/10/26
 * Desc
 */
public class MyHttpUtil {


    /**
     * <dependency>
     * <groupId>org.apache.httpcomponents</groupId>
     * <artifactId>httpclient</artifactId>
     * <version>4.5.6</version>
     * </dependency>
     *
     * <dependency>
     * <groupId>com.alibaba</groupId>
     * <artifactId>fastjson</artifactId>
     * <version>1.2.68</version>
     * </dependency>
     *
     * <dependency>
     * <groupId>junit</groupId>
     * <artifactId>junit</artifactId>
     * <version>4.12</version>
     * </dependency>
     */


    public static void main(String[] args) {

        // header
        JSONObject header = new JSONObject();
        header.put("app_id", "1256291940488445952");
        header.put("token", "2160539g1t1chs94g1t1chs5541c962fa084e71b6b5a2d81888b803");
        // body
        JSONObject param = new JSONObject();
        param.put("oozieid", "3276267-240403144153842-oozie-oozi-W");
        param.put("start_time", "2023-07-17 10:01:15");
        String s = doGet("https://gw.tcltech.com/usageTotal/service-center/service/center/call",
                JSONObject.toJavaObject(header, Map.class),
                JSONObject.toJavaObject(param, Map.class));
        System.out.println(s);

    }


    /**
     * @desc 发送get请求
     * @param: url
     * @param: param
     * @return: java.lang.String
     */
    public static String doGet(String url, Map<String, String> header, Map<String, String> param) {

        // 创建Httpclient对象
        CloseableHttpClient httpclient = HttpClients.createDefault();

        String resultString = "";
        CloseableHttpResponse response = null;
        try {
            // 创建uri
            URIBuilder builder = new URIBuilder(url);
            if (param != null) {
                for (String key : param.keySet()) {
                    builder.addParameter(key, param.get(key));
                }
            }
            URI uri = builder.build();

            // 创建http GET请求
            HttpGet httpGet = new HttpGet(uri);

            // set header
            if (header != null) {
                for (String key : header.keySet()) {
                    httpGet.setHeader(key, header.get(key));
                }
            }
            // 执行请求
            response = httpclient.execute(httpGet);
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return resultString;
    }


    /**
     * @desc 接口获取表的元数据， 并生成sr建表语句。 cdc方案生成Flink sql语句
     */
    @Test
    public void doPost5() throws Exception {

        //ods_kt_ora_omsdb_sale_prod_line(no pk) , ods_mes_zs_prp_workfactory_da
        String tableName = "ods_nc_eso_product_di";

        /**
         *  名字：获取hive入湖的原表
         *  POST  https://gw.tcltech.com/sdbg/service-center/service/center/call
         *  token：2070506g1t1chs2130307g1t1chs7fc1790cbdd74740be7ecf4fdffb0231
         *  appid：1176562931001720832
         *  请求参数：tableName
         */
        String defURL = "https://gw.tcltech.com/sdbg/service-center/service/center/call";
        URL url = new URL(defURL);
        // 打开和URL之间的连接
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");//请求post方式
        con.setUseCaches(false); // Post请求不能使用缓存
        con.setDoInput(true);// 设置是否从HttpURLConnection输入，默认值为true
        con.setDoOutput(true);// 设置是否使用HttpURLConnection进行输出，默认值为false

        //设置header内的参数
        con.setRequestProperty("Content-Type", "application/json");
//        con.setRequestProperty("isTree", "true");
//        con.setRequestProperty("isLastPage", "true");

        con.setRequestProperty("app_id", "1176562931001720832");
        con.setRequestProperty("token", "2070506g1t1chs2130307g1t1chs7fc1790cbdd74740be7ecf4fdffb0231");
        con.setRequestProperty("timestamp", "1698386896601");

        //设置body内的参数
        JSONObject param = new JSONObject();
        param.put("tableName", tableName);

        // 建立实际的连接
        con.connect();

        // 得到请求的输出流对象
        OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream(), "UTF-8");
        writer.write(param.toString());
        writer.flush();

        // 获取服务端响应，通过输入流来读取URL的响应
        InputStream is = con.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        ArrayList<String> arrayList = new ArrayList<>();
        String s;
        while ((s = reader.readLine()) != null) {
            arrayList.add(s);
        }
        reader.close();

        JSONArray datas = JSONObject.parseObject(arrayList.get(0)).getJSONArray("data");

//        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("input/aa.txt"));
        String createSRDDL = "\n----------------------------sr建表语句----------------------------" +
                "\nCREATE TABLE `" + tableName + "` (\n";
        String createMySQLDDL = "\n----------------------------flink sql脚本----------------------------" +
                "\nset 'table.exec.sink.not-null-enforcer'='DROP';\n" +
                "\n" +
                "CREATE TABLE source_table (\n";
        String insertSql = "\n\nINSERT INTO xxx \n" +
                "SELECT \n";

        String tableComment = "";
        HashSet<String> hashSet = new HashSet<>();
        ArrayList<String> primaryKeys = new ArrayList<>();
        for (Object o : datas) {
            JSONObject jsonObject = JSONObject.parseObject(o.toString());
//            String table_id = jsonObject.getString("table_id");
            String field_name = jsonObject.getString("field_name");
            String field_desc = (jsonObject.getString("field_desc") == null) ? "" : jsonObject.getString("field_desc").replace("\n", "").replace("\r", "");
            String field_is_prk = jsonObject.getString("field_is_prk");
            tableComment = jsonObject.getString("target_table_alias");

            if (hashSet.add(field_name.toLowerCase())) {
                System.out.println(o.toString());

                if ("1".equals(field_is_prk)) {
                    primaryKeys.add(field_name);
                }
                createSRDDL += "`" + field_name + "` string COMMENT '" + field_desc + "',\n";
                createMySQLDDL += "`" + field_name + "` string,\n";
                insertSql += "  `" + field_name + "`,\n";
            }
        }

        String primaryKeyStr = String.join(",", primaryKeys.stream().map(x -> "`" + x + "`").collect(Collectors.toList()));
        createSRDDL += "`op_type` varchar(255) DEFAULT NULL COMMENT '数据操作类型',\n" +
                "`s_st` varchar(255) DEFAULT NULL COMMENT '插入时间'\n" +
                ") ENGINE=OLAP \n" +
                "PRIMARY KEY(" + primaryKeyStr + ")\n" +
                "COMMENT \"" + tableComment + "\"\n" +
                "DISTRIBUTED BY HASH(" + primaryKeyStr + ") BUCKETS 32 \n" +
                "PROPERTIES (\n" +
                "\"replication_num\" = \"3\",\n" +
                "\"in_memory\" = \"false\",\n" +
                "\"storage_format\" = \"DEFAULT\",\n" +
                "\"enable_persistent_index\" = \"false\",\n" +
                "\"compression\" = \"LZ4\"\n" +
                ");";
        System.out.println(createSRDDL.toLowerCase());

        createMySQLDDL += "PRIMARY KEY(" + primaryKeyStr + ") NOT ENFORCED\n" +
                ") WITH (\n" +
                "  'connector' = 'mysql-cdc',\n" +
                "  'hostname' = '10.74.147.206',\n" +
                "  'port' = '3306',\n" +
                "  'username' = 'ogg',\n" +
                "  'password' = 'xxxxx',\n" +
                "  'database-name' = 'xxx',\n" +
                "  'table-name' = 'xxx',\n" +
                "  'scan.startup.mode' = 'initial'\n" +
                ");";
        System.out.println(createMySQLDDL);

        insertSql += "  'cdc'  as op_type,\n" +
                "  CAST(PROCTIME() AS STRING) as s_st\n" +
                "FROM\n" +
                "  source_table;";
        System.out.println(insertSql);

        // 关闭连接
        con.disconnect();

    }


    /**
     * @desc 接口获取表的元数据， 并生成sr建表语句。 大表：hive + kafka增量生成Flink sql语句，ticdc
     */
    @Test
    public void doPost6() throws Exception {

        //ods_kt_ora_omsdb_sale_prod_line(no pk) , ods_mes_zs_prp_workfactory_da
        String tableName = "ods_kt_ora_ktwms_ktwmsapp_doc_asn_header";

        /**
         *  名字：获取hive入湖的原表
         *  POST  https://gw.tcltech.com/sdbg/service-center/service/center/call
         *  token：2070506g1t1chs2130307g1t1chs7fc1790cbdd74740be7ecf4fdffb0231
         *  appid：1176562931001720832
         *  请求参数：tableName
         */
        String defURL = "https://gw.tcltech.com/sdbg/service-center/service/center/call";
        URL url = new URL(defURL);
        // 打开和URL之间的连接
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");//请求post方式
        con.setUseCaches(false); // Post请求不能使用缓存
        con.setDoInput(true);// 设置是否从HttpURLConnection输入，默认值为 true
        con.setDoOutput(true);// 设置是否使用HttpURLConnection进行输出，默认值为 false

        //设置header内的参数
        con.setRequestProperty("Content-Type", "application/json");
//        con.setRequestProperty("isTree", "true");
//        con.setRequestProperty("isLastPage", "true");

        con.setRequestProperty("app_id", "1176562931001720832");
        con.setRequestProperty("token", "2070506g1t1chs2130307g1t1chs7fc1790cbdd74740be7ecf4fdffb0231");
        con.setRequestProperty("timestamp", "1698386896601");

        //设置body内的参数
        JSONObject param = new JSONObject();
        param.put("tableName", tableName);

        // 建立实际的连接
        con.connect();

        // 得到请求的输出流对象
        OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream(), "UTF-8");
        writer.write(param.toString());
        writer.flush();

        // 获取服务端响应，通过输入流来读取URL的响应
        InputStream is = con.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        ArrayList<String> arrayList = new ArrayList<>();
        String s;
        while ((s = reader.readLine()) != null) {
            arrayList.add(s);
        }
        reader.close();

        JSONArray datas = JSONObject.parseObject(arrayList.get(0)).getJSONArray("data");

//        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("input/aa.txt"));
        String createSRDDL = "\n----------------------------sr建表语句----------------------------" +
                "\nCREATE TABLE `" + tableName + "` (\n";
        //hive全量同步脚本
        String insertSqlAll = "\n----------------------------flink sql脚本----------------------------" +
                "\nINSERT INTO\n" +
                "  xxx\n" +
                "SELECT \n";
        String createMySQLDDL = "\nSET 'table.exec.sink.not-null-enforcer' = 'drop';\n" +
                "\n" +
                "CREATE TABLE source_kafka (\n" +
                " `type` string\n" +
                ",`data`  ARRAY<map<STRING,STRING>>\n" +
                ") WITH (\n" +
                "'connector' = 'kafka'\n" +
                ",'format' = 'json'\n" +
                ",'topic' = 'xxx'\n" +
                ",'properties.bootstrap.servers' = '10.74.146.78:909210.74.146.79:9092,10.74.146.80:9092'\n" +
                ",'properties.group.id' = 'cbg_tof_etl'\n" +
                ",'scan.startup.mode' = 'earliest-offset'\n" +
                ");";

        String insertSql = "\n\nINSERT INTO\n" +
                "  xxx\n" +
                "SELECT \n";

//        String aa = "";

        String tableComment = "";
        HashSet<String> hashSet = new HashSet<>();
        ArrayList<String> primaryKeys = new ArrayList<>();
        for (Object o : datas) {
            JSONObject jsonObject = JSONObject.parseObject(o.toString());
//            String table_id = jsonObject.getString("table_id");
            String field_name = jsonObject.getString("field_name");
            String field_type = jsonObject.getString("field_type");
            String field_desc = (jsonObject.getString("field_desc") == null) ? "" : jsonObject.getString("field_desc").replace("\n", "").replace("\r", "");
            String field_is_prk = jsonObject.getString("field_is_prk");
            tableComment = jsonObject.getString("target_table_alias");

            if (hashSet.add(field_name.toLowerCase())) {
                System.out.println(o.toString());

                if ("1".equals(field_is_prk)) {
                    primaryKeys.add(field_name);
                }
                createSRDDL += "`" + field_name + "` varchar(500) COMMENT '" + field_desc + "',\n";
                insertSqlAll += "" + field_name + "`\n";
                insertSql += "  data[1]['" + field_name + "']         as " + field_name + ",\n";
            }
        }


        String primaryKeyStr = String.join(",", primaryKeys.stream().map(x -> "`" + x + "`").collect(Collectors.toList()));
        createSRDDL += "`op_type` varchar(255) DEFAULT NULL COMMENT '数据操作类型',\n" +
                "`s_st` varchar(255) DEFAULT NULL COMMENT '插入时间'\n" +
                ") ENGINE=OLAP \n" +
                "PRIMARY KEY(" + primaryKeyStr + ")\n" +
                "COMMENT \"" + tableComment + "\"\n" +
                "DISTRIBUTED BY HASH(" + primaryKeyStr + ") BUCKETS 32 \n" +
                "PROPERTIES (\n" +
                "\"replication_num\" = \"3\",\n" +
                "\"in_memory\" = \"false\",\n" +
                "\"storage_format\" = \"DEFAULT\",\n" +
                "\"enable_persistent_index\" = \"false\",\n" +
                "\"compression\" = \"LZ4\"\n" +
                ");";
        System.out.println(createSRDDL.toLowerCase());

        insertSqlAll += "  'init' as op_type,\n" +
                "  CAST(PROCTIME() AS STRING) as s_st\n" +
                "FROM\n" +
                "  xxx;";
        System.out.println(insertSqlAll);

        System.out.println(createMySQLDDL);

        insertSql += "  `type`  as op_type,\n" +
                "  CAST(PROCTIME() AS STRING) as s_st\n" +
                "FROM\n" +
                "  source_kafka;";
        System.out.println(insertSql);

        // 关闭连接
        con.disconnect();

    }


    /**
     * @desc 接口获取表的元数据， 并生成sr建表语句。 大表：hive + kafka增量生成Flink sql语句，oracle ogg
     */
    @Test
    public void doPost7() throws Exception {

        //ods_kt_ora_omsdb_sale_prod_line(no pk) , ods_mes_zs_prp_workfactory_da
        String tableName = "ods_mes_gs_factory_da";


        /**
         *  名字：获取hive入湖的原表
         *  POST  https://gw.tcltech.com/sdbg/service-center/service/center/call
         *  token：2070506g1t1chs2130307g1t1chs7fc1790cbdd74740be7ecf4fdffb0231
         *  appid：1176562931001720832
         *  请求参数：tableName
         */
        String defURL = "https://gw.tcltech.com/sdbg/service-center/service/center/call";
        URL url = new URL(defURL);
        // 打开和URL之间的连接
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");//请求post方式
        con.setUseCaches(false); // Post请求不能使用缓存
        con.setDoInput(true);// 设置是否从HttpURLConnection输入，默认值为 true
        con.setDoOutput(true);// 设置是否使用HttpURLConnection进行输出，默认值为 false

        //设置header内的参数
        con.setRequestProperty("Content-Type", "application/json");
//        con.setRequestProperty("isTree", "true");
//        con.setRequestProperty("isLastPage", "true");

        con.setRequestProperty("app_id", "1176562931001720832");
        con.setRequestProperty("token", "2070506g1t1chs2130307g1t1chs7fc1790cbdd74740be7ecf4fdffb0231");
        con.setRequestProperty("timestamp", "1698386896601");

        //设置body内的参数
        JSONObject param = new JSONObject();
        param.put("tableName", tableName);

        // 建立实际的连接
        con.connect();

        // 得到请求的输出流对象
        OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream(), "UTF-8");
        writer.write(param.toString());
        writer.flush();

        // 获取服务端响应，通过输入流来读取URL的响应
        InputStream is = con.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        ArrayList<String> arrayList = new ArrayList<>();
        String s;
        while ((s = reader.readLine()) != null) {
            arrayList.add(s);
        }
        reader.close();

        JSONArray datas = JSONObject.parseObject(arrayList.get(0)).getJSONArray("data");

//        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("input/aa.txt"));
        String createSRDDL = "\n----------------------------sr建表语句----------------------------" +
                "\nCREATE TABLE `" + tableName + "` (\n";
        //hive全量同步脚本
        String insertSqlAll = "\n----------------------------flink sql脚本----------------------------" +
                "\nINSERT INTO\n" +
                "  xxx\n" +
                "SELECT \n";
        String createMySQLDDL = "\nSET 'table.exec.sink.not-null-enforcer' = 'drop';\n" +
                "\n" +
                "CREATE TABLE source_kafka (\n" +
                " `op_type` string  --flink cdc使用op\n" +
                ",`before`  map<STRING,STRING>\n" +
                ",`after`  map<STRING,STRING>\n" +
                ") WITH (\n" +
                "'connector' = 'kafka'\n" +
                ",'format' = 'json'\n" +
                ",'topic' = 'xxx'\n" +
                ",'properties.bootstrap.servers' = '10.74.146.78:909210.74.146.79:9092,10.74.146.80:9092'\n" +
                ",'properties.group.id' = 'cbg_tof_etl'\n" +
                ",'scan.startup.mode' = 'earliest-offset'\n" +
                ");";

        String insertSql = "\n\nINSERT INTO\n" +
                "  xxx\n" +
                "SELECT \n";

        String tableComment = "";
        HashSet<String> hashSet = new HashSet<>();
        ArrayList<String> primaryKeys = new ArrayList<>();
        for (Object o : datas) {
            JSONObject jsonObject = JSONObject.parseObject(o.toString());
//            String table_id = jsonObject.getString("table_id");
            String field_name = jsonObject.getString("field_name");
            String field_desc = (jsonObject.getString("field_desc") == null) ? "" : jsonObject.getString("field_desc").replace("\n", "").replace("\r", "");
            String field_is_prk = jsonObject.getString("field_is_prk");
            tableComment = jsonObject.getString("target_table_alias");

            if (hashSet.add(field_name.toLowerCase())) {
                System.out.println(o.toString());

                if ("1".equals(field_is_prk)) {
                    primaryKeys.add(field_name);
                }
                createSRDDL += "`" + field_name + "` varchar(500) COMMENT '" + field_desc + "',\n";
                insertSqlAll += "  `" + field_name.toLowerCase() + "`,\n";
                insertSql += "   if(after is not null,after['" + field_name + "'],before['" + field_name + "'])        as " + field_name + ",\n";
            }
        }

        String primaryKeyStr = String.join(",", primaryKeys.stream().map(x -> "`" + x + "`").collect(Collectors.toList()));
        createSRDDL += "`op_type` varchar(255) DEFAULT NULL COMMENT '数据操作类型',\n" +
                "`s_st` varchar(255) DEFAULT NULL COMMENT '插入时间'\n" +
                ") ENGINE=OLAP \n" +
                "PRIMARY KEY(" + primaryKeyStr + ")\n" +
                "COMMENT \"" + tableComment + "\"\n" +
                "DISTRIBUTED BY HASH(" + primaryKeyStr + ") BUCKETS 32 \n" +
                "PROPERTIES (\n" +
                "\"replication_num\" = \"3\",\n" +
                "\"in_memory\" = \"false\",\n" +
                "\"storage_format\" = \"DEFAULT\",\n" +
                "\"enable_persistent_index\" = \"false\",\n" +
                "\"compression\" = \"LZ4\"\n" +
                ");";
        System.out.println(createSRDDL.toLowerCase());

        insertSqlAll += "  'init' as op_type,\n" +
                "  CAST(PROCTIME() AS STRING) as s_st\n" +
                "FROM\n" +
                "  xxx;\n";
        System.out.println(insertSqlAll);

        System.out.println(createMySQLDDL);

        insertSql += "  `op_type`  as op_type,\n" +
                "  CAST(PROCTIME() AS STRING) as s_st\n" +
                "FROM\n" +
                "  source_kafka;";
        System.out.println(insertSql);

        // 关闭连接
        con.disconnect();

    }


}
