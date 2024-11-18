package com.itbys.hadoop.mr_test._01_hdfs2hdfs;


import com.huaban.analysis.jieba.JiebaSegmenter;
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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Author xx
 * Date 2021/12/3
 * Desc wordcount
 */
public class TestWdTop {

    public static class Map extends Mapper<LongWritable, Text, Text, Text> {

        Text k = new Text();
        Text v = new Text();

        //定义正则匹配
        String regEx = "[ _`~!@#$%^&\\-*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：　　\t”“’。，、？]|\n|\r|\t";
        Pattern p = Pattern.compile(regEx);

        JiebaSegmenter jieba = new JiebaSegmenter();

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

            String s = value.toString();
            //切词
            List<String> strings = jieba.sentenceProcess(s);

            for (String string : strings) {
                //1、过滤长度为1的，2、过滤特殊符号
                if (string.trim().length() > 1 && !p.matcher(string.trim()).find()) {
                    k.set("1");
                    v.set(string);
                    context.write(k, v);
                }
            }
        }
    }


    public static class Reduce extends Reducer<Text, Text, Text, Text> {

        Text k = new Text();
        Text v = new Text();

        HashMap<String, Integer> hashMap = new HashMap<>();

        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {

            //将数据写入map
            for (Text value : values) {
                String word = value.toString();

                if (hashMap.containsKey(word))
                    hashMap.put(word, hashMap.get(word) + 1);
                else
                    hashMap.put(word, 1);
            }

            //排序
            ArrayList<java.util.Map.Entry<String, Integer>> entries = new ArrayList<>(hashMap.entrySet());
            entries.sort(new Comparator<java.util.Map.Entry<String, Integer>>() {
                @Override
                public int compare(java.util.Map.Entry<String, Integer> o1, java.util.Map.Entry<String, Integer> o2) {
                    return o2.getValue() - o1.getValue();
                }
            });

            //取前10
            List<java.util.Map.Entry<String, Integer>> subList = entries.subList(0, 20);

            for (java.util.Map.Entry<String, Integer> entry : subList) {

                v.set(entry.getKey() + "\t" + entry.getValue());
                context.write(null, v);
            }

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
        job.setJarByClass(TestWdTop.class);

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
        FileInputFormat.setInputPaths(job, new Path("02-hdp-base/input/"));
        FileOutputFormat.setOutputPath(job, new Path("02-hdp-base/output/01"));
        // 7 提交
        boolean result = job.waitForCompletion(true);

        System.exit(result ? 0 : 1);


        /**
         * 打包到集群跑配置
         */
//        Configuration conf = new Configuration();
//        conf.set("fs.defaultFS", "hdfs://hdp:9000");
//        String[] otherArgs = new String[]{args[0], args[1]};
//
//        Job job = Job.getInstance(conf, "TestWd");
//        job.setJarByClass(TestWdTop.class);
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
