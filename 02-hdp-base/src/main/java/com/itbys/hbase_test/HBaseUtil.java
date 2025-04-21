package com.itbys.hbase_test;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.io.compress.Compression;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Author Ari
 * Date 2023/11/11
 * Desc hive自定义函数：获取长度
 */
public class HBaseUtil {

    public static void main(String[] args) throws IOException {

        Connection connection = getConnection();


//        createTable("hbase_test","cf");

        addRecord(connection, "hbase_test", "uuid-111", "cf", "f1", "123456");

        scanTable("hbase_test").forEach(x -> System.out.println(x));

//        delete("hbase_test","uuid-111");

//        modifyRecord("hbase_test","uuid-111","cf","f1","123456");

//        listTable();

//        updateTableColfamily("Student","info","dept");

//        System.out.println(getRowCount("Student"));

//        truncateTable("Student");

    }


    /**
     * @desc 获取链接
     * @return: org.apache.hadoop.hbase.client.Connection
     */
    public static Connection getConnection() {

        Configuration conf = new Configuration();
        conf.set("hbase.zookeeper.quorum", "hdp");
        conf.set("hbase.zookeeper.property.clientPort", "2181");
        Connection connection = null;
        try {
            connection = ConnectionFactory.createConnection(conf);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return connection;

    }


    /**
     * @desc 修改列族
     * @param: table
     */
    public static void updateTableColfamily(String table, String oldColFamily, String newColFamily) throws IOException {
        Admin admin = null;
        try {
            admin = getConnection().getAdmin();
        } catch (IOException e) {
            e.printStackTrace();
        }

        TableName tableName = TableName.valueOf(table);
        HTableDescriptor tableDescriptor = admin.getTableDescriptor(tableName);

        //修改以已有的列族
        HColumnDescriptor existingColumn = new HColumnDescriptor(oldColFamily);
        existingColumn.setCompactionCompressionType(Compression.Algorithm.GZ);
        existingColumn.setMaxVersions(HConstants.ALL_VERSIONS);
        tableDescriptor.modifyFamily(existingColumn);
        //修改表的元信息
        admin.modifyTable(tableName, tableDescriptor);

        System.out.println("update table colFamily success!");

    }


    /**
     * @desc list所有表
     */
    public static void listTable() throws IOException {
        Admin admin = null;
        try {
            admin = getConnection().getAdmin();
        } catch (IOException e) {
            e.printStackTrace();
        }

        TableName[] tableNames = admin.listTableNames();
        for (TableName tableName : tableNames) {
            System.out.println(tableName.getNameAsString());
        }
    }


    /**
     * @desc 清空表
     * @param: table
     */
    public static void truncateTable(String tableName) throws IOException {
        Admin admin = null;
        try {
            admin = getConnection().getAdmin();
        } catch (IOException e) {
            e.printStackTrace();
        }

        admin.disableTable(TableName.valueOf(tableName));
        admin.truncateTable(TableName.valueOf(tableName), true);

        System.out.println("truncate table success!");

    }

    /**
     * @desc 创建表
     * @param: table
     */
    public static void createTable(String tableName, String colFamily) throws IOException {
        Admin admin = null;
        try {
            admin = getConnection().getAdmin();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //存在先删除表
        if (tableExists(tableName)) {
            admin.disableTable(TableName.valueOf(tableName));
            admin.deleteTable(TableName.valueOf(tableName));
        }

        TableName table = TableName.valueOf(tableName);//定义表名
        HTableDescriptor htd = new HTableDescriptor(table);//定义表对象
        HColumnDescriptor hcd = new HColumnDescriptor(colFamily);//定义列族对象
        htd.addFamily(hcd); //添加
        admin.createTable(htd);//创建表

        System.out.println("create table success!");

    }

    /**
     * @desc 判断表存在
     * @param: table
     * @return: boolean
     */
    public static boolean tableExists(String tableName) throws IOException {

        Admin admin = null;
        try {
            admin = getConnection().getAdmin();
        } catch (IOException e) {
            e.printStackTrace();
        }
        TableName table = TableName.valueOf(tableName);
        return admin.tableExists(table);

    }

    /**
     * @desc 查询表的行数
     * @param: tab
     * @return: java.lang.Integer
     */
    public static String getRowCount(String tab) throws IOException {
        //获取表，创建scan
        Table table = getConnection().getTable(TableName.valueOf(tab));
        ResultScanner scanner = table.getScanner(new Scan());

        HashSet<String> hashSet = new HashSet<>();

        //遍历数据
        for (Result result : scanner) {
            List<Cell> listCells = result.listCells();
            for (Cell cell : listCells) {
                //获取rowkey，值
                String rowkey = Bytes.toString(CellUtil.cloneRow(cell));
                hashSet.add(rowkey);
            }
        }
        return tab + "行数：" + hashSet.size();
    }


    /**
     * @desc 扫描数据
     * @param: tab
     * @return: java.com.tcl.com.itbys.util.ArrayList<java.lang.String>
     */
    public static ArrayList<String> scanTable(String tab) throws IOException {
        //获取表，创建scan
        Table table = getConnection().getTable(TableName.valueOf(tab));
        ResultScanner scanner = table.getScanner(new Scan());

        ArrayList<String> arrayList = new ArrayList<>();

        //遍历数据
        for (Result result : scanner) {
            List<Cell> listCells = result.listCells();
            for (Cell cell : listCells) {
                //获取rowkey，值
                String rowkey = Bytes.toString(CellUtil.cloneRow(cell));
                String colFamliy = Bytes.toString(CellUtil.cloneFamily(cell));
                String column = Bytes.toString(CellUtil.cloneQualifier(cell));
                String value = Bytes.toString(CellUtil.cloneValue(cell));

                String info = "rowkey: " + rowkey + ", colFamliy: " + colFamliy + ", column: " + column + ", value: " + value;
                arrayList.add(info);
            }
        }
        return arrayList;
    }


    /**
     * @desc 插入、变更数据
     * @param: tableName
     * @param: row
     * @param: colFamily
     * @param: field
     * @param: value
     */
    public static void addRecord(Connection conn, String tableName, String row, String colFamily, String field, String value) throws IOException {

        //添加数据
        Table table = conn.getTable(TableName.valueOf(tableName));
        Put put = new Put(Bytes.toBytes(row));

        put.addColumn(Bytes.toBytes(colFamily), Bytes.toBytes(field), Bytes.toBytes(value));
        table.put(put);

        System.out.println("addRecord success!");

    }


    /**
     * @desc 删除行
     * @param: tableName
     * @param: rowkey
     */
    public static void delete(String tableName, String rowkey) throws IOException {

        //1.获取表对象
        Table table = getConnection().getTable(TableName.valueOf(tableName));

        //2.构建删除对象
        Delete delete = new Delete(Bytes.toBytes(rowkey));

        //2.1设置删除的列
//        delete.addColumn(Bytes.toBytes(family),Bytes.toBytes(qualifier));
        //2.2删除指定的列族
        //delete.addFamily(Bytes.toBytes(family));

        //3.执行删除操作
        table.delete(delete);

        //4.关闭表连接
        table.close();

        System.out.println("delete rowkey: " + rowkey + " success!");

    }


}
