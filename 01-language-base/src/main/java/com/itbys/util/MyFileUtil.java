package com.itbys.util;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Author chenjie
 * Date 2024/2/26
 * Desc file operate com.tcl.com.itbys.util
 */
public class MyFileUtil {

    public static void main(String[] args) {

        modifyFileName("00-doc/src/01_bigData_notes/db-relationship");

    }


    /**
     * @desc 批量修改文件名
     * @param: dir
     */
    public static void modifyFileName(String dir) {
        File file = new File(dir);
        File[] files = file.listFiles();

        if (Objects.nonNull(files) && files.length > 0) {
            for (File file1 : files) {
                if (file1.isDirectory()) {
                    continue;
                } else {
                    String fileName = file1.getName();
                    File parentPath = file1.getParentFile();
                    String[] params = fileName.split("\\.");
                    String newName = fileName;//初始值
                    if (params.length >= 2) {
                        newName = params[0].replace("笔记", "_notes.") + params[1];
                    }
                    File newDir = new File(parentPath + "/" + newName);
                    file1.renameTo(newDir);
                    System.out.println(parentPath.toPath() + "\\" + fileName + "修改完成");
                }
            }
        }
    }

    /**
     * @desc 读取文件
     * @param: filePath
     */
    public static ArrayList<String> readFile(String filePath) throws IOException {

        BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));

        ArrayList<String> arrayList = new ArrayList<>();
        String s;
        while ((s = bufferedReader.readLine()) != null) {
            arrayList.add(s);
        }
        bufferedReader.close();

        return arrayList;

    }


    /**
     * @desc 写入文件
     * @param: filePath
     */
    public static void writeFile(String filePath) throws IOException {

        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filePath));

        bufferedWriter.write("abc");
        bufferedWriter.newLine();

        bufferedWriter.close();

    }


    /**
     * @desc 转换文件编码
     * @param: sourcePath
     * @param: destPath
     */
    public static void convertCode(String sourcePath, String destPath) throws IOException {

        sourcePath = "C:\\Users\\chenjie\\Desktop\\14001_mr_test\\origin_data\\C34-Economy";// 文件夹源路径
        destPath = sourcePath + "C:\\Users\\chenjie\\Desktop\\14001_mr_test\\origin_data\\C34-Economy2";

        File sourceDirectory = new File(sourcePath);
        File destDirectory = new File(destPath);
        if (!sourceDirectory.isDirectory()) {
            return;
        }
        // 获取文件夹中的所有.java文件，包括所有子级文件夹中的文件
        Collection<File> files = FileUtils.listFiles(sourceDirectory, new String[]{"txt", "TXT"}, true);
        for (File file : files) {
            String absolutePath = file.getAbsolutePath();
            String newDir = absolutePath.replace(sourceDirectory.getName(), destDirectory.getName());
            // 把单个文件从gbk编码转化到utf-8编码，生成新文件，可以自动创建父级目录
            FileUtils.writeLines(new File(newDir), "UTF-8", FileUtils.readLines(file, "GBK"));
        }
        // 删除源目录,子文件都删除
        FileUtils.deleteQuietly(sourceDirectory);
        // 把生成文件目录重命名成源目录名
        destDirectory.renameTo(new File(sourceDirectory.getAbsolutePath()));
        System.out.println("success");

    }

}
