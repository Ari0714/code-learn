package com.sc.app.ck;

import com.sc.common.CommonConfig;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;

/**
 * Author xx
 * Date 2023/9/4
 * Desc ck data auto sync
 */
public class CkAutoMigrate {

    public static void main(String[] args) throws SQLException, IOException {

        //pre
//        Connection sourConn = getCkConn(CommonConfig.CkGroupPreIp, CommonConfig.CkGroupPreUser, CommonConfig.CkGroupPrePassWd);
//        Connection destConn = getCkConn(CommonConfig.CkIndustryPreIp, CommonConfig.CkIndustryPreUser, CommonConfig.CkIndustryPrePassWd);

        //prod
        Connection sourConn = getCkConn(CommonConfig.CkGroupProdIp, CommonConfig.CkGroupProdUser, CommonConfig.CkGroupProdPassWd);
        Connection destConn = getCkConn(CommonConfig.CkIndustryProdIp, CommonConfig.CkIndustryProdUser, CommonConfig.CkIndustryProdPassWd);

//        syncTableSchema(sourConn, destConn);
        syncData(sourConn, destConn, args[0]);

    }


    /**
     * @desc auto sync data
     */
    public static void syncData(Connection sourConn, Connection destConn, String db) throws SQLException, IOException {

        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("/opt/test-chenjie/02-sync-ck-tableData/syncData_" + db + ".txt"));

        ResultSet resultSet = null;
        try {
            resultSet = sourConn.prepareStatement("SELECT database, name, create_table_query, partition_key, engine = 'View' AS is_view\n" +
                    "                       FROM system.tables\n" +
                    "                       WHERE database NOT IN ('system')\n" +
                    "                     and database = '"+db+"'\n" +
                    "                     and is_view = 0\n" +
                    "                       ORDER BY if(engine = 'View', 999, 0), database, name").executeQuery();

//            resultSet = sourConn.prepareStatement("" +
//                    "   SELECT database, name, create_table_query, partition_key, engine = 'View' AS is_view\n" +
//                    "    FROM system.tables\n" +
//                    "    WHERE database NOT IN ('system')\n" +
//                    "    and database = 'default'\n" +
//                    "    and name = 'dm_leiniao_application_d'\n" +
//                    "    ORDER BY if(engine = 'View', 999, 0), database, name").executeQuery();

//            resultSet = sourConn.prepareStatement("\n" +
//                    "SELECT database, name, create_table_query, partition_key, engine = 'View' AS is_view\n" +
//                    "                                       FROM system.tables\n" +
//                    "                                          WHERE database NOT IN ('system')\n" +
//                    "                                           and database = 'default'\n" +
//                    "                                          and name in ('dm_leiniao_application_d','student','summtt')\n" +
//                    "                                          ORDER BY if(engine = 'View', 999, 0), database, name").executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        while (resultSet.next()) {
            String dbTable = resultSet.getString(1) + ".`" + resultSet.getString(2) + "`";
//            System.out.println(dbTable);

            String trunSql = "truncate table " + dbTable + ";";
            String insertSql = "INSERT INTO " + dbTable + "\n" +
                    "SELECT * FROM remote('10.74.146.161', '" + dbTable.replace("`", "") + "', 'admin', 'clkadmin');";

            //execute migrate
            try {
                int pps01 = destConn.prepareStatement(trunSql).executeUpdate();
                if (1 == pps01) {
                    System.out.println(dbTable + ": truncate data success !!!");
                    bufferedWriter.write(dbTable + ": truncate data success !!!\n");
                    int pps02 = destConn.prepareStatement(insertSql).executeUpdate();
                    if (1 == pps02) {
                        System.out.println(dbTable + ": sync data success !!!");
                        bufferedWriter.write(dbTable + ": sync data success !!!\n");
                    } else {
                        System.out.println(dbTable + ": sync data fail !!!");
                        bufferedWriter.write(dbTable + ": sync data fail !!!\n");
                    }
                } else {
                    System.out.println(dbTable + ": truncate data fail !!!");
                    bufferedWriter.write(dbTable + ": truncate data fail !!!\n");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println(dbTable + ": error, sync data fail !!!");
                bufferedWriter.write(dbTable + ": error, sync data fail !!!\n" + e.getMessage()+"\n");
            }

            bufferedWriter.flush();

        }

        bufferedWriter.close();

    }


    /**
     * @desc sync table schema
     */
    public static void syncTableSchema(Connection sourConn, Connection destConn) throws SQLException, IOException {

        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("/opt/test-chenjie/01-sync-ck-tableSchema/syncTableSchema.txt"));

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
                bufferedWriter.write(db + "." + table + " ===> error，update fail\n");
            }
            if (i == 1) {
                System.out.println(db + "." + table + " ===> update success");
                bufferedWriter.write(db + "." + table + " ===> update success\n");
            } else {
                System.out.println(db + "." + table + " ===> update fail");
                bufferedWriter.write(db + "." + table + " ===> update fail\n");
            }

        }

        bufferedWriter.close();

    }


    /**
     * @desc generate sync data ddl
     */
    public static void GenerateSyncDataDDL(Connection sourConn, Connection destConn, String db) throws SQLException, IOException {

        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("00-tcl/input/GenerateSyncDataDDL.txt"));

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
     * 建立ck连接
     */
    public static Connection getCkConn(String url, String user, String password) {

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

    //mark
    /**
     * 【execute】 differ 【executeUpdate】
     * 1、execute =》 return true represent select，reture false represent delete、insert、update，
     * 2、executeUpdate =》 return int，represent effect rows
     */

}
