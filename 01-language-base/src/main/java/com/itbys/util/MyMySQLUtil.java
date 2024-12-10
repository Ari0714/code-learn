package com.itbys.util;

import java.sql.*;

/**
 * Author xx
 * Date 2023/9/4
 * Desc
 */
public class MyMySQLUtil {

    public static void main(String[] args) throws SQLException {

//        System.out.println(getConn().getMetaData());

    }


    /**
     * create connection
     */
    public static Connection getConn(String ip, String username, String db, String passwd, int port) {

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
                    "jdbc:mysql://" + ip + ":" + port + "/" + db + "?useUnicode=true&characterEncoding=utf8", username, passwd);
        } catch (SQLException e) {
            System.out.println("connect fail !!!");
            e.printStackTrace();
        }

        return conn;

    }


    /**
     * @desc insert single line
     */
    public static void test01(Connection conn) {
        Statement stmt = null;
        PreparedStatement ps = null;
        try {
            // 注册 JDBC 驱动
            Class.forName("com.mysql.jdbc.Driver");
            // 打开链接
            //实例化Statement对象
            stmt = conn.createStatement();
            String sql;
            sql = "INSERT INTO login VALUES(?,?)";

            ps = conn.prepareStatement(sql);//添加数据预处理
            ps.setString(1, "5433");
            ps.setString(2, "214544");
            ps.executeUpdate();
            // 完成后关闭
            ps.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException ignored) {
            }
            try {
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }

    }


    /**
     * @desc batch insert
     */
    public static void test02(Connection conn) throws ClassNotFoundException, SQLException {

        //加载驱动
        Class.forName("com.mysql.jdbc.Driver");

//        long start = System.currentTimeMillis();
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
