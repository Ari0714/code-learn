package com.itbys.java_basics._09_io;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Author chenjie
 * Date 2024/2/26
 * Desc
 */
public class _05_fileInputStream_copypic {

    public static void main(String[] args) throws IOException {

        FileInputStream fileInputStream = new FileInputStream("D:\\study\\home\\tiger.jpg");
        FileOutputStream fileOutputStream = new FileOutputStream("D:\\study\\home\\tiger_copy.jpg", false);

        int date;
//        while ((date=fileInputStream.read()) != -1){
//            fileOutputStream.write(date);
//        }

        //
        byte[] bytes = new byte[10];
        while ((date = fileInputStream.read(bytes)) != -1) {
            for (int i = 0; i < date; i++)
                fileOutputStream.write(bytes[i]);
        }

        fileInputStream.close();
        fileOutputStream.close();
    }

}
