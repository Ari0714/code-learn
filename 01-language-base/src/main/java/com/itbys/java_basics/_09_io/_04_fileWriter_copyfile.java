package com.itbys.java_basics._09_io;

import org.junit.Test;

import java.io.*;

/**
 * Author chenjie
 * Date 2024/2/26
 * Desc
 */
public class _04_fileWriter_copyfile {

    public static void main(String[] args) throws IOException {

        FileInputStream fileInputStream = new FileInputStream("D:\\study\\home\\eagle安装手册.txt");
        FileOutputStream fileOutputStream = new FileOutputStream("D:\\study\\home\\eagle安装手册_test.txt", false);

        int date;
        while ((date = fileInputStream.read()) != -1) {
            fileOutputStream.write(date);
        }

        fileInputStream.close();
        fileOutputStream.close();
    }

    @Test
    public void test02() throws IOException {

        FileReader fileReader = new FileReader("D:\\study\\home\\eagle安装手册.txt");
        FileWriter fileWriter = new FileWriter("D:\\study\\home\\eagle安装手册_test02.txt", false);

        int date;
        while ((date = fileReader.read()) != -1) {
            fileWriter.write(date);
        }

        fileReader.close();
        fileWriter.close();


    }

}
