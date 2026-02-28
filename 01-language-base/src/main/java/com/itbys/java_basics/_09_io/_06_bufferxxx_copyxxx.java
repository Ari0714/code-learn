package com.itbys.java_basics._09_io;

import org.junit.Test;

import java.io.*;

/**
 * Author chenjie
 * Date 2024/2/26
 * Desc
 */
public class _06_bufferxxx_copyxxx {

    public static void main(String[] args) throws IOException {

        BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream("D:\\study\\home\\tiger.jpg"));
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream("D:\\study\\home\\tiger_test.jpg"));

        int date;
        while ((date = bufferedInputStream.read()) != -1) {
            bufferedOutputStream.write(date);
        }

        bufferedInputStream.close();
        bufferedOutputStream.close();
    }


    @Test
    public void test02() throws IOException {

        BufferedReader bufferedReader = new BufferedReader(new FileReader("D:\\study\\home\\eagle安装手册.txt"));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("D:\\study\\home\\eagle安装手册_test.txt"));

        int date;
        while ((date = bufferedReader.read()) != -1) {
            bufferedWriter.write(date);
        }

        bufferedReader.close();
        bufferedWriter.close();

    }
}
