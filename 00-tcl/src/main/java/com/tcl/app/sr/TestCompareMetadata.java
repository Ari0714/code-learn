package com.tcl.app.sr;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Author xx
 * Date 2023/7/9
 * Desc compare metadata
 */
public class TestCompareMetadata {

    public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {

//        getCompare();

        if (args.length == 0) {
            System.out.println("no parameter error");
        } else {
            if ("audit".equals(args[0])) {
                getAuditLog(args[1]);
            } else if ("compare".equals(args[0])) {
                getCompare();
            } else {
                System.out.println("parameter input error");
            }
        }

    }


    /**
     * @desc compare metadata
     */
    public static void getCompare() throws ClassNotFoundException, SQLException, IOException {

        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("/opt/test_chenjie/01_sr_metadata_update/compareMetadata.txt"));

        ArrayList<String> techTablesList = new ArrayList<>();
        ArrayList<String> bdpTablesList = new ArrayList<>();

        //加载驱动
        Class.forName("com.mysql.jdbc.Driver");
        //连接数据库
//        Connection conn1 = DriverManager.getConnection("jdbc:mysql://10.74.134.171:8081?useUnicode=true&characterEncoding=utf8",
//                "root", "mpp@getech123"); //集团测试
        Connection conn2 = DriverManager.getConnection("jdbc:mysql://10.74.146.201:8080?useUnicode=true&characterEncoding=utf8",
                "root", "mpp@getech123"); //集团正式

//        Connection conn3 = DriverManager.getConnection("jdbc:mysql://10.74.64.164:9030?useUnicode=true&characterEncoding=utf8",
//                "root", "aa865728-0524-42e7-8319-a7a51a7b11d1"); //实业测试
        Connection conn4 = DriverManager.getConnection("jdbc:mysql://10.91.2.108:9030?useUnicode=true&characterEncoding=utf8",
                "root", "165c7e01-97cd-4433-85bf-92f967a863d8"); //实业正式

        //创建命令执行对象
        Statement stmt = conn2.createStatement();
        String sql = "select concat(table_schema,'.',table_name) from information_schema.tables\n" +
                "where table_schema in(\n" +
                "'data_service_platform'\n" +
                ",'rw_header'\n" +
                ",'rw_sensitive'\n" +
                ",'tcl_com'\n" +
                ",'rw_tcloms'\n" +
                ",'tpch'\n" +
                ",'rw_cbgdata'\n" +
                ",'ssb'\n" +
                ",'ods'\n" +
                ",'rw_hhdata'\n" +
                ",'voc'\n" +
                ",'rw_ktdata'\n" +
                ",'tcl_sdbg_data_governance'\n" +
                ",'temp'\n" +
                ",'uat_hr'\n" +
                ",'data_service'\n" +
                ",'test_pre'\n" +
                ",'starrocks_monitor'\n" +
                ",'rw_uidata'\n" +
                ",'rw_obgdata'\n" +
                ",'tcl_bigdata_api'\n" +
                ",'meta_data'\n" +
                ",'ods_all'\n" +
                ",'tcl_bigdata_secret_api'\n" +
                ",'dim'\n" +
                ",'tcl_dm_data'\n" +
                ",'test'\n" +
                ",'tcl_realtime'\n" +
                ",'dwd')" +
                "\n";
        System.out.println(sql);

        //执行对象执行SQL语句
        ResultSet resultSet = stmt.executeQuery(sql);
        while (resultSet.next()) {
            String dbTable = resultSet.getString(1);
            techTablesList.add(dbTable);
        }

        //创建命令执行对象
        Statement stmt2 = conn4.createStatement();

        //执行对象执行SQL语句
        ResultSet resultSet2 = stmt2.executeQuery(sql);
        while (resultSet2.next()) {
            String dbTable = resultSet2.getString(1);
            bdpTablesList.add(dbTable);
        }

        List<String> compareList = techTablesList.stream().filter(x -> !bdpTablesList.contains(x)).sorted().collect(Collectors.toList());
        for (String s : compareList) {
            System.out.println(s);
            bufferedWriter.write(s + "\n");
            bufferedWriter.flush();
        }

        bufferedWriter.close();

        //关闭资源
        stmt.close();
        stmt2.close();
//        conn.close();

    }


    /**
     * @desc get sr audit log
     */
    public static void getAuditLog(String dt) throws ClassNotFoundException, SQLException, IOException {

        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("/opt/test_chenjie/01_sr_metadata_update/srMetadata_" + dt + ".txt"));

        //加载驱动
        Class.forName("com.mysql.jdbc.Driver");
        //连接数据库
//        Connection conn = DriverManager.getConnection("jdbc:mysql://10.74.134.171:8081/" + dbName + "?useUnicode=true&characterEncoding=utf8",
//                "root", "mpp@getech123"); //集团测试
        Connection conn = DriverManager.getConnection("jdbc:mysql://10.74.146.201:8080/starrocks_audit_db__?useUnicode=true&characterEncoding=utf8",
                "root", "mpp@getech123"); //集团正式

        //创建命令执行对象
        //select db, stmt from (
        //select db, stmt from starrocks_audit_tbl__
        //where DATE_FORMAT(`timestamp`,'%Y-%m-%d') = '2024-08-19'
        //and isQuery = 0
        //and (lower(stmt) like 'create%' or lower(stmt) like 'alter%' or lower(stmt) like 'drop%'
        //or lower(stmt) like 'set%')
        //order by `timestamp`
        //) t
        //group by db, stmt
        //order by db
        Statement stmt = conn.createStatement();
        String sql = "select db, stmt from (\n" +
                "select db, stmt from starrocks_audit_tbl__\n" +
                "where DATE_FORMAT(`timestamp`,'%Y-%m-%d') = '" + dt + "'\n" +
                "and isQuery = 0\n" +
                "and (lower(stmt) like 'create%' or lower(stmt) like 'alter%' or lower(stmt) like 'drop%')\n" +
                "and lower(stmt) not like '%swap with%'\n" +
                "and lower(stmt) not like '%rename%'\n" +
                "order by `timestamp`\n" +
                ") t\n" +
                "group by db, stmt\n" +
                "order by db";
        System.out.println(sql);

        //执行对象执行SQL语句
        ResultSet resultSet = stmt.executeQuery(sql);
        while (resultSet.next()) {
//            Statement statement = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            String auditdb = resultSet.getString(1);
            String auditStmt = resultSet.getString(2);
            bufferedWriter.write(auditdb + "\t" + auditStmt + "\n\n");
            System.out.println(auditStmt+"\n");

        }

        bufferedWriter.close();

        //关闭资源
        stmt.close();
        conn.close();


    }


}
