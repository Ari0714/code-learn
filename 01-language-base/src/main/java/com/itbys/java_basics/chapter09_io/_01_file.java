package com.itbys.java_basics.chapter09_io;

import java.io.File;

/**
 * Author chenjie
 * Date 2024/2/26
 * Desc
 */
public class _01_file {

    public static void main(String[] args) {

        File file = new File("D:\\study\\home\\eagle安装手册.txt");

        String name = file.getName();
        String absolutePath = file.getAbsolutePath();
        long length = file.length();
        System.out.println(length);
        long l = file.lastModified();
        boolean b = file.canRead();
        boolean exists = file.exists();
        boolean file1 = file.isFile();

    }


}
