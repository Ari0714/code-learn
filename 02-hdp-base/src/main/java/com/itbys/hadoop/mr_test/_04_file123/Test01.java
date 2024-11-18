package com.itbys.hadoop.mr_test._04_file123;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.util.HashMap;

/**
 * Author xx
 * Date 2023/2/18
 * Desc 文件file1，2，3 wordcount
 */
public class Test01 {

    public static class Map extends Mapper<LongWritable, Text, Text, Text> {

        Text k = new Text();
        Text v = new Text();

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

            String s = value.toString();
            String[] strings = s.split(" ");

            //获取指定的文件
            FileSplit inputSplit = (FileSplit) context.getInputSplit();
            String fileName = inputSplit.getPath().getName();

            for (String string : strings) {
                k.set(string);
                v.set(fileName);
                context.write(k, v);
            }
        }
    }

    public static class Reduce extends Reducer<Text, Text, Text, Text> {

        Text v = new Text();
        HashMap<String, Integer> hashMap = new HashMap<>();

        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {

            String word = key.toString();

            //将数据写入map
            for (Text value : values) {
                String fileName = value.toString();

                if (hashMap.containsKey(fileName))
                    hashMap.put(fileName, hashMap.get(fileName) + 1);
                else
                    hashMap.put(fileName, 1);
            }

            // 整理格式
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(word + "\t");
            for (java.util.Map.Entry<String, Integer> stringIntegerEntry : hashMap.entrySet()) {
                stringBuffer.append(stringIntegerEntry.getKey() + "=" + stringIntegerEntry.getValue() + ";");
            }

            //写出
            v.set(stringBuffer.toString());
            context.write(null, v);

        }
    }


    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {

        /**
         * 本地运行
         */
        // 1 获取配置信息以及封装任务
        Configuration configuration = new Configuration();
        Job job = Job.getInstance(configuration);

        // 2 设置jar加载路径
        job.setJarByClass(Test01.class);

        // 3 设置map和reduce类
        job.setMapperClass(Map.class);
        job.setReducerClass(Reduce.class);

        // 4 设置map输出
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);

        // 5 设置最终输出kv类型
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        // 6 设置输入和输出路径
        FileInputFormat.setInputPaths(job, new Path("input/"));
        FileOutputFormat.setOutputPath(job, new Path("output/01"));
        // 7 提交
        boolean result = job.waitForCompletion(true);

        System.exit(result ? 0 : 1);


        /**
         * 打包到集群跑配置
         */
//        Configuration conf = new Configuration();
//        conf.set("fs.defaultFS", "hdfs://hdp:8020");
//        String[] otherArgs = new String[]{args[0], args[1]};
//
//        Job job = Job.getInstance(conf, "Test01");
//        job.setJarByClass(Test01.class);
//
//        job.setMapperClass(Map.class);
//        job.setReducerClass(Reduce.class);
//
//        job.setOutputKeyClass(Text.class);
//        job.setOutputValueClass(Text.class);
//
//        FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
//        FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));
//
//        System.exit(job.waitForCompletion(true) ? 0 : 1);

    }


}
