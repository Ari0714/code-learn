package com.itbys.java_basics.chapter03_exception;


import java.io.FileInputStream;
import java.io.IOException;

/**
 * Author xx
 * Date 2023/3/14
 * Desc
 */
public class _03_exception_finally {

    //finally 最后要关闭
    public static void main(String[] args) {

        FileInputStream fis = null;

        try {
            fis = new FileInputStream("D:\\data\\test_doc\\com.itbys.java_basics.txt");

            int read;
            while ((read = fis.read()) != -1) {
                System.out.println((char) read);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
