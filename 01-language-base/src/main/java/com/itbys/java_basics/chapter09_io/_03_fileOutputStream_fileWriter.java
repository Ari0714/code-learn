package com.itbys.java_basics.chapter09_io;

import org.junit.Test;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Author chenjie
 * Date 2024/2/26
 * Desc
 */
public class _03_fileOutputStream_fileWriter {

    public static void main(String[] args) throws IOException {

        FileOutputStream fileOutputStream = new FileOutputStream("D:\\study\\home\\eagle安装手册_test.txt", false);
        fileOutputStream.write("i hava a dream".getBytes());
        fileOutputStream.close();

    }


    @Test
    public void test02() throws IOException {

        //指定append 或 overwrite
        FileWriter fileWriter = new FileWriter("D:\\study\\home\\eagle安装手册_test02.txt", false);

        fileWriter.write("i hava a dream\n");
        fileWriter.write("i hava a dream,too");

        //需要close或flush
        fileWriter.close();

    }


}
