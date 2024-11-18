package com.itbys.util;

import org.junit.Test;

import java.io.*;

/**
 * Author xx
 * Date 2023/9/2
 * Desc
 */
public class TclIpParseUtil {

    public static void main(String[] args) {

        System.out.println(ip2long("113.87.160.5"));

    }

    /**
     * @desc ip to long
     * @param: ip
     * @return: java.lang.Long
     */
    private synchronized static Long ip2long(String ip) {
        long result = 0l;
        String[] ipSegment = ip.split("\\.");
        //ip格式校验
        if (ipSegment.length != 4) {
            System.out.println("ip 格式错误！！！");
        }
        for (int i = 0; i < ipSegment.length; i++) {
            result += Long.valueOf(ipSegment[i].isEmpty() ? "0" : ipSegment[i]) << (8 * (3 - i));
        }
        return result;
    }


    /**
     * @desc 清洗ip数据
     */
    @Test
    public void testIpData() throws IOException {

        BufferedReader bufferedReader = new BufferedReader(new FileReader("C:\\Users\\jie173.chen\\Desktop\\tcl_文件\\数据服务\\ip\\24-02月\\world\\world.csv"));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("C:\\Users\\jie173.chen\\Desktop\\tcl_文件\\数据服务\\ip\\24-02月\\world\\world2.txt"));

        String s;
        while ((s = bufferedReader.readLine()) != null) {
            if (!s.contains("\"ip_start\",\"ip_end\"") && s.contains("中国")) {
                String[] split = s.split(",");
                if (split.length > 10) {
//                    System.out.println(s);
//                    System.out.println(split.length);
                    String info = split[0] + "," + split[1] + "," + split[10] + "," + split[11];
                    System.out.println(info.replace("\"", ""));
                    bufferedWriter.write(info.replace("\"", ""));
                    bufferedWriter.newLine();
                }
            }
        }

        bufferedWriter.close();
    }

}
