package com.sc.app.hudi;

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
public class TestTclUnitHudi {

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
     * @desc 数据比对
     */
    @Test
    public void testCompare() throws IOException {

        HashMap<String, String> hudiMap = testCompareMap("input/tcl/kt/input_compare_hudi.txt");
        HashMap<String, String> sourceMap = testCompareMap("input/tcl/kt/input_compare_source.txt");

        String sRigth = "";
        String sError = "";
        String notCompare = "";
        for (Map.Entry<String, String> stringStringEntry : sourceMap.entrySet()) {
            if (hudiMap.containsKey(stringStringEntry.getKey())) {
                if (stringStringEntry.getValue().equals(hudiMap.get(stringStringEntry.getKey()))) {
                    sRigth += stringStringEntry.getKey() + ": source: " + stringStringEntry.getValue() + ", hudi: " + hudiMap.get(stringStringEntry.getKey()) + "\n";
                } else {
                    sError += stringStringEntry.getKey() + ": source: " + stringStringEntry.getValue() + ", hudi: " + hudiMap.get(stringStringEntry.getKey()) + "\n";
                }
            } else {
                notCompare += stringStringEntry.getKey() + "\n";
            }
        }
        System.out.println(sRigth + "\n" + sError + "\n" + notCompare);
    }


    /**
     * @desc
     * @param: file
     * @return: java.com.tcl.com.itbys.util.HashMap<java.lang.String                                                                                                                                                                                                                                                               ,                                                                                                                                                                                                                                                               java.lang.String>
     */
    public HashMap<String, String> testCompareMap(String file) throws IOException {

        HashMap<String, String> map = new HashMap<>();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

        String s;
        while ((s = bufferedReader.readLine()) != null) {
            String[] split = s.split("\t");
            map.put(split[0], split[1]);
        }
        return map;
    }

    /**
     * @desc
     * @param: file
     * @return: java.com.tcl.com.itbys.util.HashMap<java.lang.String                                                                                                                                                                                                                                                               ,                                                                                                                                                                                                                                                               java.lang.String>
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

}
