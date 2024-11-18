package com.itbys.hadoop.hdfs_test;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.IOUtils;

import java.io.*;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Author xx
 * Date 2023/2/18
 * Desc
 */
public class HdfsUtil {

    public static void main(String[] args) throws Exception {

        //1 创建连接
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", "hdfs://hdp:8020");

        //本地上传
//        uploadFile(conf, "input/uploadTest.txt", "/test");

        //删除文佳 true 为递归删除 否则为非递归
//        deleteFile(conf, "/test/uploadTest.txt", true);

        /*
         * 文件管理部分功能
         */
        //创建文件夹
//        mkDirs(conf, "/test/hdfsTest");
//
//        //新建文件
//        createFile(conf, "/test/aa.txt", "sss".getBytes());
//
//        //读取文件
//        System.out.println(readFile(conf, "/test/uploadTest.txt"));
//
//        //移动文佳
//        //需要注意权限问题 hadoop fs -chmod 777 /test/*
//        mvFile(conf, "/test/uploadTest.txt", "/test/hdfsTest/uploadTest.txt");
//
//        //复制文件
//        copyFile(conf, "/test/hdfsTest/uploadTest.txt", "/test/");
//
//        //下载文件
//        download("/test/hdfsTest/uploadTest.txt", "/test/", conf);

        //追加文件
//        appendStr("/test/uploadTest.txt", "abcd", conf);

        lsFile(conf, "/test");

    }


    /**
     * @desc ls命令
     * @param: conf
     * @param: desFile
     */
    public static void lsFile(Configuration conf, String desFile) throws IOException {
        FileSystem fs = FileSystem.get(URI.create(desFile), conf);
        FileStatus[] status = fs.listStatus(new Path(desFile));
        for (int i = 0; i < status.length; i++) {
            long time = status[i].getModificationTime();
            Date date = new Date(time);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String data = sdf.format(date);
            System.out.println("路径: " + status[i].getPath() + "   文件大小: " + status[i].getLen() + "   权限: " + status[i].getPermission() + "  文件创建时间: " + data);
        }
    }


    /**
     * @desc 下载到本地
     * @param: souFile
     * @param: destFile
     * @param: conf
     */
    public static void download(String souFile, String destFile, Configuration conf) throws IOException {
        //构建FileSystem
        FileSystem fs = FileSystem.get(URI.create(souFile), conf);
        //读取文件
        InputStream is = fs.open(new Path(souFile));
        //保存到本地  最后 关闭输入输出流
        IOUtils.copyBytes(is, new FileOutputStream(new File(destFile)), 2048, true);

        System.out.println("download success ！！！");
        fs.close();

    }

    /**
     * @desc 追加 - 字符
     * @param: hdfs_path
     * @param: appendStr
     * @param: conf
     */
    private static void appendStr(String hdfs_path, String appendStr, Configuration conf) throws IOException {

        FileSystem fs = FileSystem.get(URI.create(hdfs_path), conf);

        //要追加的文件流，appendStr为字符
        InputStream in = new ByteArrayInputStream(appendStr.getBytes());
        OutputStream out = fs.append(new Path(hdfs_path));
        IOUtils.copyBytes(in, out, 4096, true);

        System.out.println("appendStr success ！！！");
        fs.close();

    }

    /**
     * @desc 追加 - 文件
     * @param: hdfs_path
     * @param: loc_inpath
     * @param: conf
     */
    private static void append(String hdfs_path, String loc_inpath, Configuration conf) throws IOException {

        FileSystem fs = FileSystem.get(URI.create(hdfs_path), conf);

        //要追加的文件流，inpath为文件
        InputStream in = new BufferedInputStream(new FileInputStream(loc_inpath));
        OutputStream out = fs.append(new Path(hdfs_path));
        IOUtils.copyBytes(in, out, 4096, true);

        System.out.println("append success ！！！");
        fs.close();

    }

    /**
     * @desc 复制文件
     * @param: configuration
     * @param: source
     * @param: target
     */
    public static void copyFile(Configuration configuration, String source, String target) throws IOException {
        FileSystem fileSystem = FileSystem.get(configuration);
        FileUtil.copy(fileSystem, new Path(source), fileSystem, new Path(target), false, configuration);

        System.out.println("copyFile success ！！！");
        fileSystem.close();
    }

    /**
     * @desc 移动文件
     * @param: configuration
     * @param: path
     * @param: newPath
     */
    public static void mvFile(Configuration configuration, String path, String newPath) throws Exception {
        boolean result = false;
        FileSystem fs = null;
        try {
            fs = FileSystem.get(configuration);
            if (!fs.exists(new Path(newPath))) {
                result = fs.rename(new Path(path), new Path(newPath));
            } else {
                System.out.println("HDFS上目录" + newPath + "被占用！");
            }
        } catch (Exception e) {
            System.out.println("移动HDFS上目录" + path + "失败！");
            e.printStackTrace();
        }
        if (result)
            System.out.println("mvFile success ！！！");
    }


    /**
     * @desc 创建文件夹
     * @param: configuration
     * @param: filePath
     */
    public static void mkDirs(Configuration configuration, String filePath) throws IOException {
        FileSystem fileSystem = FileSystem.get(configuration);
        fileSystem.mkdirs(new Path(filePath));

        System.out.println("mkDirs success ！！！");
        fileSystem.close();
    }

    /**
     * @desc 新建文件
     * @param: configuration
     * @param: filePath
     * @param: data
     */
    public static void createFile(Configuration configuration, String filePath, byte[] data) throws IOException {
        FileSystem fileSystem = FileSystem.get(configuration);
        FSDataOutputStream fsDataOutputStream = fileSystem.create(new Path(filePath));
        fsDataOutputStream.write(data);
        fsDataOutputStream.close();

        System.out.println("create success ！！！");
        fileSystem.close();
    }

    /**
     * @desc 删除文佳 true 为递归删除 否则为非递归
     * @param: configuration
     * @param: filePath
     * @param: isReturn
     */
    public static void deleteFile(Configuration configuration, String filePath, boolean isReturn) throws IOException {
        FileSystem fileSystem = FileSystem.get(configuration);
        boolean delete = fileSystem.delete(new Path(filePath), isReturn);
        if (!delete) {
            throw new RuntimeException("删除失败");
        }

        System.out.println("delete success ！！！");
        fileSystem.close();
    }

    /**
     * @desc 读取文件内容
     * @param: conf
     * @param: filePath
     * @return: java.lang.String
     */
    public static String readFile(Configuration conf, String filePath) throws IOException {
        String res = null;
        FileSystem fs = null;
        FSDataInputStream inputStream = null;
        org.apache.commons.io.output.ByteArrayOutputStream outputStream = null;
        try {
            fs = FileSystem.get(conf);
            inputStream = fs.open(new Path(filePath));
            outputStream = new ByteArrayOutputStream(inputStream.available());
            IOUtils.copyBytes(inputStream, outputStream, conf);
            res = outputStream.toString();
        } finally {
            if (inputStream != null)
                IOUtils.closeStream(inputStream);
            if (outputStream != null)
                IOUtils.closeStream(outputStream);
        }
        return res;
    }

    /**
     * @desc 上传文件
     * @param: configuration
     * @param: localFilePath
     * @param: remoteFilePath
     */
    public static void uploadFile(Configuration configuration, String localFilePath, String remoteFilePath) throws IOException {
        FileSystem fileSystem = FileSystem.get(configuration);
        fileSystem.copyFromLocalFile(new Path(localFilePath), new Path(remoteFilePath));

        System.out.println("upload success ！！！");
        fileSystem.close();
    }

    /**
     * @desc 判断目录是否存在
     * @param: configuration
     * @param: filePath
     * @return: boolean
     */
    public static boolean fileExists(Configuration configuration, String filePath) throws IOException {
        FileSystem fileSystem = FileSystem.get(configuration);
        return fileSystem.exists(new Path(filePath));
    }

}
