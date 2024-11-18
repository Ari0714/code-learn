package com.itbys.hadoop.mr_test._05_self_partiton;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Partitioner;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.util.Arrays;

/**
 * Author xx
 * Date 2023/4/6
 * Desc 计算每个城市流量和并倒排序，结果输出两个文件，苏锡常镇为一组，其他城市为一组
 */
public class Test01 {

    public static class EmpPartitioner extends Partitioner<Text, Text> {

        public int getPartition(Text text, Text text2, int numPartitions) {
            if (Arrays.asList("苏州市", "无锡市", "常州市", "镇江市").contains(text.toString())) {
                return 0;
            } else {
                //上述条件不满足情况下，给定一个分区
                return 1;
            }

        }
    }

    public static class Map extends Mapper<LongWritable, Text, Text, Text> {

        Text k = new Text();
        Text v = new Text();

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

            //input：9	859	y_z_a	user-1	passwd09	Jack	3`1-2`	'116,117'	109	192.168.8.9	苏州市	part1_i|part2_I|part3_int_18
            String s = value.toString();

            //过滤空行
            if (s.trim().length() > 1) {
                k.set(s.split("\t")[10]);
                v.set(s);
                context.write(k, v);
            }
        }
    }

    public static class Reduce extends Reducer<Text, Text, Text, Text> {

        Text v = new Text();

        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {

            //定义销售总额、笔数
//            double saleAmount = 0d;
            int saleCnt = 0;

            //累加数据
            for (Text value : values) {
                String[] strings = value.toString().split("\t");
//                saleAmount += Double.parseDouble(strings[2]);
                saleCnt++;
            }

            // 写出
            v.set(key + "\t" + saleCnt);
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

        job.setNumReduceTasks(2);
        job.setPartitionerClass(EmpPartitioner.class);

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
     /*   Configuration conf = new Configuration();
        conf.set("fs.defaultFS", "hdfs://hdp:9000");
        String[] otherArgs = new String[]{args[0], args[1]};

        Job job = Job.getInstance(conf, "Test01");
        job.setJarByClass(Test01.class);

        job.setMapperClass(Map.class);
        job.setReducerClass(Reduce.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
        FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));

        System.exit(job.waitForCompletion(true) ? 0 : 1);*/

    }


}
