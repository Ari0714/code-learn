package com.itbys.hive_test;

import java.sql.*;

/**
 * Author xx
 * Date 2023/4/21
 * Desc
 */
public class HiveJdbc {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {

        hiveJdbc();

    }


    /**
     * @desc jdbc测试
     */
    public static void hiveJdbc() throws ClassNotFoundException, SQLException {

        Class.forName("org.apache.hive.jdbc.HiveDriver");
        Connection conn = DriverManager.getConnection("jdbc:hive://192.168.192.138:10000/hive_xx", "", "");

        // 查询数据
        PreparedStatement prep = conn.prepareStatement("select * from stu");
        ResultSet resultSet = prep.executeQuery();
        while (resultSet.next()) {
            String name = resultSet.getString(1);
            int age = resultSet.getInt(2);
            System.out.println(String.format("name：{}, age：{}", name, age));
        }

    }

}
