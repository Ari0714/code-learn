package com.itbys.java_basics.chapter09_io;

import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;

/**
 * Author chenjie
 * Date 2024/2/26
 * Desc
 */
public class _02_fileInputStream_fileRead {

    public static void main(String[] args) throws IOException {

        FileInputStream fileInputStream = new FileInputStream("D:\\study\\home\\eagle安装手册.txt");

        int date;
//        while ((date=fileInputStream.read())!=-1)
//            System.out.println((char) date);

        //改进
        byte[] bytes = new byte[5];
        while ((date = fileInputStream.read(bytes)) != -1) {
            for (int i = 0; i < date; i++)
                System.out.println((char) bytes[i]);
        }

        fileInputStream.close();

    }

    @Test
    public void test02() throws IOException {

        FileReader fileReader = new FileReader("D:\\study\\home\\eagle安装手册.txt");

        int date;
//        while ((date=fileReader.read()) != -1){
//            System.out.println((char)date);
//        }

        //改进
        char[] chars = new char[10];
        while ((date = fileReader.read(chars)) != -1) {
            for (int i = 0; i < date; i++)
                System.out.println(chars[i]);
        }

        fileReader.close();

    }

}
