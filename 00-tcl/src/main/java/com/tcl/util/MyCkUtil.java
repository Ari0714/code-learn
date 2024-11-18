package com.tcl.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;

/**
 * Author xx
 * Date 2023/9/4
 * Desc
 */
public class MyCkUtil {

    //# source_conn = Client(host='10.74.134.149', user='admin', password='clkadmin', port=8123)
    //# target_conn = Client(host='10.68.20.29', user='admin', password='clkadmin', port=9000)

    public static void main(String[] args) throws SQLException, IOException {

        Connection sourConn = getConn("jdbc:clickhouse://10.74.134.149:8123?socket_timeout=600000", "admin", "clkadmin");
        Connection destConn = getConn("jdbc:clickhouse://10.68.20.29:8123?socket_timeout=600000", "admin", "clkadmin");

//        syncTable();
        syncData("ogg_poc_tgt_db");

    }


    /**
     * @desc 同步数据
     */
    public static void syncData(String db) throws SQLException, IOException {

        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("01-language-base/input/syncData.txt"));

        Connection sourConn = getConn("jdbc:clickhouse://10.74.134.149:8123?socket_timeout=600000", "admin", "clkadmin");
        Connection destConn = getConn("jdbc:clickhouse://10.68.20.29:8123?socket_timeout=600000", "admin", "clkadmin");

        ResultSet resultSet = null;
        try {
            resultSet = sourConn.prepareStatement("SELECT database, name, create_table_query, partition_key, engine = 'View' AS is_view\n" +
                    "    FROM system.tables\n" +
                    "    WHERE database NOT IN ('system')\n" +
                    "    and database = '" + db + "'\n" +
                    "    ORDER BY if(engine = 'View', 999, 0), database, name").executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        while (resultSet.next()) {
            String dbTable = resultSet.getString(1) + ".`" + resultSet.getString(2) + "`";
//            System.out.println(dbTable);

            String trunSql = "truncate table " + dbTable + ";";
            String insertSql = "INSERT INTO " + dbTable + "\n" +
                    "SELECT * FROM remote('10.74.134.149', '" + dbTable.replace("`", "") + "', 'admin', 'clkadmin');";
            System.out.println(trunSql);
            System.out.println(insertSql + "\n");

            bufferedWriter.write(trunSql + "\n");
            bufferedWriter.write(insertSql + "\n" + "\n");
            bufferedWriter.flush();

        }

        bufferedWriter.close();

    }


    /**
     * @desc 同步表结构
     */
    public static void syncTable() throws SQLException, IOException {

        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("01-language-base/input/error.txt"));

        Connection sourConn = getConn("jdbc:clickhouse://10.74.134.149:8123?socket_timeout=600000", "admin", "clkadmin");
        Connection destConn = getConn("jdbc:clickhouse://10.68.20.29:8123?socket_timeout=600000", "admin", "clkadmin");

        ResultSet resultSet = null;
        try {
            resultSet = sourConn.prepareStatement("SELECT database, name, create_table_query, partition_key, engine = 'View' AS is_view\n" +
                    "    FROM system.tables\n" +
                    "    WHERE database NOT IN ('system')\n" +
                    "    ORDER BY if(engine = 'View', 999, 0), database, name").executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        while (resultSet.next()) {
            String db = resultSet.getString(1);
            String table = resultSet.getString(2);
            String createDDL = resultSet.getString(3)
                    .replace("CREATE TABLE ", "CREATE TABLE IF NOT EXISTS ")
                    .replace("CREATE VIEW ", "CREATE VIEW IF NOT EXISTS ")
                    .replace("CREATE MATERIALIZED VIEW ", "CREATE MATERIALIZED VIEW IF NOT EXISTS ");
//            System.out.println(createDDL);

            int i = 0;
            try {
                i = destConn.prepareStatement(createDDL).executeUpdate();
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (i == 1) {
                System.out.println(db + "." + table + " ===> update success");
            } else {
                System.out.println(db + "." + table + " ===> update fail");
                bufferedWriter.write(db + "." + table + " ===> update fail\n");
            }

        }

        bufferedWriter.close();

    }


    /**
     * 建立连接
     */
    public static Connection getConn(String url, String user, String password) {

        try {
            Class.forName("ru.yandex.clickhouse.ClickHouseDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return conn;

    }


    /**
     * @desc 批量插入
     */
    public static void BatchInsect() throws ClassNotFoundException, SQLException {

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
