package com.tcl.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Author xx
 * Date 2023/9/4
 * Desc
 */
public class MyMySQLUtil {

    public static void main(String[] args) throws SQLException {

        System.out.println(getConn().getMetaData());

    }


    /**
     * 建立连接
     */
    public static Connection getConn() {

        //加载驱动
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        //连接数据库
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(
                    "jdbc:mysql://10.74.65.10:40001/bdp-admin-pre?useUnicode=true&characterEncoding=utf8",
                    "daas_admin", "FX2FD0MgdJfm");
        } catch (SQLException e) {
            System.out.println("connect fail !!!");
            e.printStackTrace();
        }

        return conn;

    }


    /**
     * @desc 批量插入
     */
    public static void batchInsert() throws ClassNotFoundException, SQLException {

        //加载驱动
        Class.forName("com.mysql.jdbc.Driver");

//        long start = System.currentTimeMillis();
        Connection conn = DriverManager.getConnection("jdbc:mysql://hdp:4000/test?useUnicode=true&characterEncoding=utf8",
                "root", "");
        String sql = "insert into orders values(?,now(),\"gong\",13.99,123,1);";
        PreparedStatement ps = null;

        try {
            ps = conn.prepareStatement(sql);
            for (int i = 2544620; i <= 50000000; i++) {
                ps.setObject(1, i);
                ps.addBatch();//将sql语句打包到一个容器中
                if (i % 1000 == 0) {
                    ps.executeBatch();//将容器中的sql语句提交
                    ps.clearBatch();//清空容器，为下一次打包做准备

                    System.out.println("插入" + i);
                }
            }
            //为防止有sql语句漏提交【如i结束时%500！=0的情况】，需再次提交sql语句
            ps.executeBatch();//将容器中的sql语句提交
            ps.clearBatch();//清空容器
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            conn.close();
        }
//        System.out.println("百万条数据插入用时：" + (System.currentTimeMillis() - start)+"【单位：毫秒】");

    }

}
