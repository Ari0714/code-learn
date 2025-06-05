package com.sc.app.sr;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;

/**
 * Author xx
 * Date 2023/9/4
 * Desc
 */
public class TestGetSRDDL {

    public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {

        //加载驱动
        Class.forName("com.mysql.jdbc.Driver");
        //连接数据库
        Connection conn = DriverManager.getConnection("jdbc:mysql://10.74.134.171:8081/rw_cbgdata?useUnicode=true&characterEncoding=utf8",
                "root", "mpp@getech123");
        //创建命令执行对象
        Statement stmt = conn.createStatement();
        String sql = "\n" +
                "select table_name, table_schema, engine from information_schema.tables\n" +
                "where table_schema = 'rw_cbgdata' and (table_type = 'VIEW')\n";
        //执行对象执行SQL语句
        ResultSet resultSet = stmt.executeQuery(sql);
        while (resultSet.next()){
            Statement statement = conn.createStatement();
            String table_name = resultSet.getString(1);
            String table_schema = resultSet.getString(2);
            String engine = resultSet.getString(3);
            if (engine.equals("StarRocks")){
                ResultSet resultSet1 = statement.executeQuery("show CREATE MATERIALIZED view " + table_name + "");
                while (resultSet1.next()){
                    System.out.println(resultSet1.getString(1));
                    writeFile("/opt/TestGetSRDDL.txt",resultSet1.getString(1));
                    writeFile("/opt/TestGetSRDDL.txt",resultSet1.getString(2));
                }
            }
            else {
                ResultSet resultSet1 = statement.executeQuery("show CREATE view " + table_name + "");
                while (resultSet1.next()){
                    System.out.println(resultSet1.getString(1));
                    writeFile("/opt/TestGetSRDDL.txt",resultSet1.getString(1));
                    writeFile("/opt/TestGetSRDDL.txt",resultSet1.getString(2));
                }
            }
            statement.close();
        }

        //关闭资源
        stmt.close();
        conn.close();

    }


    /**
     * @desc 写入文件
     * @param: filePath
     */
    public static void writeFile(String filePath, String text) throws IOException {

        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filePath));

        bufferedWriter.write(text);
        bufferedWriter.newLine();

        bufferedWriter.close();

    }



}
