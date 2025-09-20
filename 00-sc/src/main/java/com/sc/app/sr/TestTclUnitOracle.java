package com.sc.app.sr;

import org.junit.Test;

import java.io.*;
import java.util.ArrayList;

/**
 * Author Ari
 * Date 2023/9/2
 * Desc
 */
public class TestTclUnitOracle {

    /**
     * @desc oracle建表语句 to sr建表语句
     */
    @Test
    public void testOracleDDLToSRDDL() throws IOException {

        String info = "";
        String primaryKey = "";
        String tableName = "";
        ArrayList<String> arrayList = readFile("input/tcl/oracle/ddl_2_insert_init_oracle.txt");
        String info2 = "";
        for (String s : arrayList) {
            if (s.contains("CREATE TABLE")) {
                tableName = s.split("CREATE TABLE ")[1].replace("\" (", "")
                        .replace("\"", "").replaceAll("\\.", "_");
                info += "create table " + tableName.toLowerCase() + "(\n";
            }
            if (s.contains("\"") && !s.contains("INDEX") && !s.contains("CREATE TABLE") && !s.contains("CONSTRAINT")) {
                String[] s1 = s.replace("\"", "`").split(" ");
                info2 += "  " + s1[0] + " string,\n";
            }
            if (s.contains("CONSTRAINT")) {
                //CONSTRAINT "PK_SALE_PROD_H_LINE" PRIMARY KEY ("PROD_ID", "PROD_H_ID")
                primaryKey = s.split("\\(")[1].replace(")", "").replace("\"", "");
                if (primaryKey.contains(",")) {
                    primaryKey = primaryKey.replace(", ", "`, `");
                }
//                System.out.println(primaryKey);
//                info2 += "  INDEX index_"+primaryKey+" (`"+primaryKey+"`) USING BITMAP\n";
            }
        }
        String info3 = "  `op_type` varchar(255) DEFAULT NULL COMMENT '数据操作类型',\n" +
                "  `s_st` varchar(255) DEFAULT NULL COMMENT '插入时间'\n" +
                ") ENGINE=OLAP\n" +
                "PRIMARY KEY(`" + primaryKey + "`)\n" +
                "COMMENT \"\"\n" +
                "DISTRIBUTED BY HASH(`" + primaryKey + "`) BUCKETS 32 \n" +
                "PROPERTIES (\n" +
                "\"replication_num\" = \"3\",\n" +
                "\"in_memory\" = \"false\",\n" +
                "\"storage_format\" = \"DEFAULT\",\n" +
                "\"enable_persistent_index\" = \"false\",\n" +
                "\"compression\" = \"LZ4\"\n" +
                ");";
        System.out.println((info + info2 + info3).toLowerCase());

    }


    /**
     * @desc init语句
     */
    @Test
    public void testddlToInsert() throws IOException {

        String info = "";
        info += "INSERT INTO\n" +
                "  xxx \n" +
                "SELECT\n";
        ArrayList<String> arrayList = readFile("input/tcl/oracle/ddl_2_insert_init_oracle.txt");
        String info2 = "";
        for (String s : arrayList) {
            if (s.contains("\"") && !s.contains("INDEX") && !s.contains("CREATE TABLE") && !s.contains("CONSTRAINT")) {
                String[] s1 = s.replace("\"", "`").split(" ");
                info2 += "  " + s1[0] + ",\n";
            }
        }
        String info3 = "  'init' as op_type,\n" +
                "  CAST(PROCTIME() AS STRING) as s_st\n" +
                "FROM\n" +
                "  xxx;";
        System.out.println((info + info2 + info3).toLowerCase());

    }


    /**
     * @desc oracle 增量语句
     */
    @Test
    public void testDeleteOracle() throws IOException {

        BufferedReader bufferedReader = new BufferedReader(new FileReader("input/tcl/oracle/input_delete_oracle.txt"));

        String info = "INSERT INTO\n" +
                "  xxx \n" +
                "select \n";

        String s;
        while ((s = bufferedReader.readLine()) != null) {
            String replace = s.trim().replace("'' as ", "").replace("`", "");
            if (replace.contains(",") && !replace.equals("'init' as op_type,") && !replace.equals("cast(proctime() as string) as s_st")) {
                String replace2 = replace.trim().replace(",", "").toUpperCase();
                String s1 = "  if(after is not null,after['" + replace2 + "'],before['" + replace2 + "'])        as `" + replace2 + "`,";
                info += s1 + "\n";

            }
        }
        String info2 = "  `op`  as op_type,\n" +
                "  CAST(PROCTIME() AS STRING) as s_st\n" +
                "FROM\n" +
                "  source_kafka;";
        System.out.println(info + info2);
    }


    /**
     * @desc
     * @param: file
     * @return: java.com.tcl.com.itbys.util.ArrayList<java.lang.String>
     */
    public ArrayList<String> readFile(String file) throws IOException {
        ArrayList<String> arrayList = new ArrayList<>();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

        String s;
        while ((s = bufferedReader.readLine()) != null) {
            arrayList.add(s);
        }
        return arrayList;
    }


    public static void main(String[] args) throws IOException {

        System.out.println(" oms.sale_prod_header".replaceAll("\\.", "_"));

    }


}
