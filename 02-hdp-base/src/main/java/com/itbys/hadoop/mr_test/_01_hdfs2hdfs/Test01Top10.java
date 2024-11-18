package com.itbys.hadoop.mr_test._01_hdfs2hdfs;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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

/**
 * Author xx
 * Date 2022/7/19
 * Desc 柱状图：top10站点
 */
public class Test01Top10 {

    public static class Map extends Mapper<LongWritable, Text, Text, Text> {

        Text k = new Text();
        Text v = new Text();

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

            String s = value.toString();

            //不同站点发送到不同reduce key
            if (s.trim().length() > 0 && s.contains("station")) {

                JSONObject jsonObject = JSON.parseObject(s);
                String station = jsonObject.getString("station");

                k.set("1");
                v.set(station);
                context.write(k, v);
            }
        }
    }


    public static class Reduce extends Reducer<Text, Text, Text, Text> {

        Text v = new Text();
        HashMap<String, Integer> hashMap = new HashMap<>();  //对全局key起作用，map中k.set("1");

        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {

            //将数据写入map, 累加
            for (Text value : values) {
                String station = value.toString();

                if (hashMap.containsKey(station))
                    hashMap.put(station, hashMap.get(station) + 1);
                else
                    hashMap.put(station, 1);
            }

            //排序
            ArrayList<java.util.Map.Entry<String, Integer>> entries = new ArrayList<>(hashMap.entrySet());
            entries.sort(new Comparator<java.util.Map.Entry<String, Integer>>() {
                @Override
                public int compare(java.util.Map.Entry<String, Integer> o1, java.util.Map.Entry<String, Integer> o2) {
                    return o2.getValue() - o1.getValue();
                }
            });

            //取出top10
            List<java.util.Map.Entry<String, Integer>> subList = entries.subList(0, 10);

            //写出
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
        job.setJarByClass(Test01Top10.class);

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
        FileOutputFormat.setOutputPath(job, new Path("02-hdp-base/output/05"));
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
//        Job job = Job.getInstance(conf, "Test01Hbase2Db");
//        job.setJarByClass(Test01Top10.class);
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
