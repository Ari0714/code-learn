package com.itbys.hadoop.mr_test._01_hdfs2hdfs;


import com.google.common.collect.Lists;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 * Author xx
 * Date 2022/11/3
 * Desc wordcount
 */
public class TestWd {

    public static class Map extends Mapper<LongWritable, Text, Text, Text> {

        Text k = new Text();
        Text v = new Text();

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

            String s = value.toString();
            String[] strings = s.split(" ");

            for (String string : strings) {
                if(string.trim().length() > 0){
                    k.set(string);
                    v.set("1");
                    context.write(k, v);
                }
            }
        }
    }


    public static class Reduce extends Reducer<Text, Text, Text, Text> {

        Text v = new Text();

        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {

            int size = Lists.newArrayList(values).size();

            v.set(key + "\t" + size);
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
        job.setJarByClass(TestWd.class);

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
        FileInputFormat.setInputPaths(job, new Path("02-hdp-base/input/access.log"));
        FileOutputFormat.setOutputPath(job, new Path("02-hdp-base/output/01"));

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
//        Job job = Job.getInstance(conf, "TestWd");
//        job.setJarByClass(TestWd.class);
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
