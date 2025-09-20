package com.sc.app.sr;

import com.sc.util.MyDateUtil;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;

/**
 * Author Ari
 * Date 2023/6/19
 * Desc get sr metastore
 */
public class TestGetSRMetadata {

    public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {

        if (args.length > 0) {
            TestGrant(args[0]);
        } else {
            TestGrant(MyDateUtil.getYYYYMMDDSub1());
        }

    }


    /**
     * @desc get sr audit log
     */
    public static void TestGrant(String dt) throws ClassNotFoundException, SQLException, IOException {

        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("/opt/test_chenjie/srMetadata_" + dt + ".txt"));

        //加载驱动
        Class.forName("com.mysql.jdbc.Driver");
        //连接数据库
//        Connection conn = DriverManager.getConnection("jdbc:mysql://10.74.134.171:8081/" + dbName + "?useUnicode=true&characterEncoding=utf8",
//                "root", "mpp@getech123"); //集团测试
        Connection conn = DriverManager.getConnection("jdbc:mysql://10.74.146.201:8080/starrocks_audit_db__?useUnicode=true&characterEncoding=utf8",
                "root", "mpp@getech123"); //集团正式

        //创建命令执行对象
        Statement stmt = conn.createStatement();
        String sql = "select db, stmt from (\n" +
                "select db, stmt from starrocks_audit_tbl__\n" +
                "where DATE_FORMAT(`timestamp`,'%Y-%m-%d') = '"+dt+"'\n" +
                "and isQuery = 0\n" +
                "and (lower(stmt) like 'create%' or lower(stmt) like 'alter%' or lower(stmt) like 'drop%'  \n" +
                "or lower(stmt) like 'set%')\n" +
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
            bufferedWriter.write(auditdb + "\t" + auditStmt + "\n");
            System.out.println(auditStmt);

        }

        bufferedWriter.close();

        //关闭资源
        stmt.close();
        conn.close();


    }


}
