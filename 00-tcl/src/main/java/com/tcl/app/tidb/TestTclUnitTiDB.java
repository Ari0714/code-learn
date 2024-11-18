package com.tcl.app.tidb;

import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Author xx
 * Date 2023/9/2
 * Desc
 */
public class TestTclUnitTiDB {

    public static void main(String[] args) throws IOException {

        BufferedReader bufferedReader = new BufferedReader(new FileReader("01-language-base/input/tcl/input.txt"));

        String s;
        while ((s = bufferedReader.readLine()) != null) {
            String s1 = "select '" + s + "', count(*) cnt\n" +
                    "from ods." + s + "/*+ OPTIONS('read.streaming.enabled' = 'false')*/\n" +
                    "where creation_date<=from_unixtime(unix_timestamp(CAST(PROCTIME() AS STRING))-3600,'yyyy-MM-dd HH')\n" +
                    "union all \n";
            System.out.println(s1);
        }

    }


    /**
     * @desc ddl to init sql语句
     */
    @Test
    public void testddlToInsert() throws IOException {

        String info = "";
        info += "INSERT INTO\n" +
                "  xxx\n" +
                "SELECT\n";
        ArrayList<String> arrayList = readFile("input/tcl/tidb/ddl_2_insert_init_tidb.txt");
        String info2 = "";
        for (String s : arrayList) {
            if (s.contains("`") && !s.contains("INDEX") && !s.contains("CREATE") && !s.contains("PRIMARY")) {
                String[] s1 = s.split(" ");
                info2 += "  " + s1[0].toLowerCase() + ",\n";
            }
        }
        String info3 = "  'init' as op_type,\n" +
                "  CAST(PROCTIME() AS STRING) as s_st\n" +
                "FROM\n" +
                "  xxx";
        System.out.println(info.toLowerCase() + info2.toLowerCase() + info3.toLowerCase());

    }


    /**
     * @desc tidb 硬删除拼接
     */
    @org.junit.Test
    public void testDelete() throws IOException {

        BufferedReader bufferedReader = new BufferedReader(new FileReader("input/tcl/tidb/ddl_2_insert_init_tidb.txt"));

        String info = "INSERT INTO\n" +
                "  ods_hudi_tof_ticdc_tof_order_virtual_order_relational\n" +
                "select \n";

        //'' as
        String s;
        while ((s = bufferedReader.readLine()) != null) {
            String replace = s.trim().replace(",", "").replace("'' as ", "")
                    .replace("`", "");
            if (s.contains(",") && !s.contains("s_st") && !s.contains("op_type")) {
                String s1 = "  data[1]['" + replace + "']         as " + replace + ",";
                info += s1 + "\n";
            }
        }
        String info2 = "  `type`  as op_type,\n" +
                "  CAST(PROCTIME() AS STRING) as s_st\n" +
                "FROM\n" +
                "  source_kafka;";
        System.out.println(info + info2);

    }

    public ArrayList<String> readFile(String file) throws IOException {
        ArrayList<String> arrayList = new ArrayList<>();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

        String s;
        while ((s = bufferedReader.readLine()) != null) {
            arrayList.add(s);
        }
        return arrayList;
    }

}