package com.itbys.util;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Author Ari
 * Date 2023/10/16
 * Desc
 */
public class MyDataXUtil {

    @Test
    public void test01() throws IOException {

        HashMap<String, String> hashMap = new HashMap<>();
        HashSet<String> hashSet = new HashSet<>();

        ArrayList<String> strings = readFile("input/input.txt");
        for (String string : strings) {
//            String[] split = string.split("\t");
            if (string.trim().length() > 0 && string.contains(",")) {
//                hashSet.add(string.trim());
                String replace = string.replace(",", "");
                System.out.println(",replace(" + replace + ",'\\u0000', '') " + replace);

            }
        }
//        for (String s : hashSet) {
//            System.out.println(s);
//        }

//        System.out.println(hashSet.size());

//        //排序
//        ArrayList<java.com.tcl.com.itbys.util.Map.Entry<String, String>> entries = new ArrayList<>(hashMap.entrySet());
//        entries.sort(new Comparator<java.com.tcl.com.itbys.util.Map.Entry<String, String>>() {
//            @Override
//            public int compare(java.com.tcl.com.itbys.util.Map.Entry<String, String> o1, java.com.tcl.com.itbys.util.Map.Entry<String, String> o2) {
//                return o2.getValue().hashCode() - o1.getValue().hashCode();
//            }
//        });
//
//        for (Map.Entry<String, String> entry : entries) {
//            System.out.println(entry.toString());
//        }

//        hashMap.entrySet().forEach(x -> System.out.println(x));

    }


    //读取格创导出的表结构 -》 sr建表语句，初始化语句
    @Test
    public void test02() throws IOException {

        //"work_no\twork_no\tSTRING\t工单号\t否\t\t";
        String createSRDDL = "\n----------------------------sr建表语句----------------------------" +
                "\nCREATE TABLE `xxx` (\n";

        //hive全量同步脚本
        String insertSqlAll = "\n----------------------------flink sql脚本----------------------------" +
                "\nINSERT INTO\n" +
                "  xxx\n" +
                "SELECT \n";

        ArrayList<String> arrayList = readFile("input/getechDDl2SRDDL.txt");
        for (String s : arrayList) {
            String[] split = s.split("\t");
            String field_name = split[0];
            String field_desc = split[3];
//            createSRDDL += "`" + field_name + "` string COMMENT '" + field_desc + "',\n";

            createSRDDL += "`" + field_name + "` varchar(500),\n";

            insertSqlAll += "  `" + field_name.toLowerCase() + "`,\n";
        }

        createSRDDL += "`op_type` varchar(255) DEFAULT NULL COMMENT '数据操作类型',\n" +
                "`s_st` varchar(255) DEFAULT NULL COMMENT '插入时间'\n" +
                ") ENGINE=OLAP \n" +
                "PRIMARY KEY(xxx)\n" +
                "COMMENT \"xxx\n" +
                "DISTRIBUTED BY HASH(xxx) BUCKETS 32 \n" +
                "PROPERTIES (\n" +
                "\"replication_num\" = \"3\",\n" +
                "\"in_memory\" = \"false\",\n" +
                "\"storage_format\" = \"DEFAULT\",\n" +
                "\"enable_persistent_index\" = \"false\",\n" +
                "\"compression\" = \"LZ4\"\n" +
                ");";
        System.out.println(createSRDDL.toLowerCase());

        insertSqlAll += "  'init' as op_type,\n" +
                "  CAST(PROCTIME() AS STRING) as s_st\n" +
                "FROM\n" +
                "  xxx;\n";
        System.out.println(insertSqlAll);


    }


    //判断字段对不上的
    @Test
    public void test03() throws IOException {

        HashMap<String, Integer> hashMap = new HashMap<>();
        HashSet<String> hashSet = new HashSet<>();
        ArrayList<String> arrayList = readFile("input/getechDDl2SRDDL.txt");
        for (String s : arrayList) {
            String[] split = s.split(",");
            for (String s1 : split) {
                if (!s1.contains("op_type") && !s1.contains("s_st")) {
                    String string = s1.toLowerCase().trim().replace("string", "").replace("`", "");

                    if (hashMap.containsKey(string))
                        hashMap.put(string, hashMap.get(string) + 1);
                    else
                        hashMap.put(string, 1);
                }
            }
        }

        for (Map.Entry<String, Integer> stringIntegerEntry : hashMap.entrySet()) {
            if (stringIntegerEntry.getValue() == 1) {
                System.out.println(stringIntegerEntry.getKey());
            }

        }

    }


    /**
     * @desc
     * @param: file
     * @return: java.com.tcl.com.itbys.util.ArrayList<java.lang.String>
     */
    public static ArrayList<String> readFile(String file) throws IOException {

        ArrayList<String> strings = new ArrayList<>();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

        String s;
        while ((s = bufferedReader.readLine()) != null) {
            strings.add(s);
        }
        return strings;
    }


    public static void main(String[] args) throws IOException {
        ArrayList<String> strings = readFile("01-language-base/input/input.txt");
        for (String string : strings) {
            if (string.trim().toLowerCase().startsWith("ads_")) {
                String s = "select '" + string + "' table_name, count(*) cnt from " + string + "\n" +
                        "union all";
                System.out.println(s);
            }
        }

    }


}



