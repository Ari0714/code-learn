package com.itbys.hadoop.mr_test._03_hbase;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;

import java.io.IOException;

/**
 * Author xx
 * Date 2023/4/28
 * Desc MapReduce读取数据写入hbase
 */
public class Test03Hbase2Hbase {

    public static class Map extends TableMapper<Text, Text> {

        Text k = new Text();
        Text v = new Text();

        @Override
        protected void map(ImmutableBytesWritable key, Result value, Mapper<ImmutableBytesWritable, Result, Text, Text>.Context context) throws IOException, InterruptedException {

            //input: 排名	车名	品牌	车型	评分	评价1	评价2	评价3
            String clazz = Bytes.toString(value.getValue("info".getBytes(), "all".getBytes()));

            if (!clazz.contains("评分")) {
                String[] strings = clazz.split("\t");
                context.write(new Text(strings[2]), new Text(strings[4].replace("分", "")));
            }

        }
    }


    public static class Reduce extends TableReducer<Text, Text, NullWritable> {

        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {

            int i = 1;
            for (Text value : values) {
                String s = value.toString();

                Put put = new Put(Bytes.toBytes(String.valueOf(i++)));
                put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("f1"), Bytes.toBytes(String.valueOf(s)));
                context.write(null, put);
            }

        }
    }


    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {

        /**
         * 本地运行
         */
        // 1 获取配置信息以及封装任务
        Configuration config = HBaseConfiguration.create();
        config.set("hbase.zookeeper.quorum", "hdp");
        config.set("hbase.zookeeper.property.clientPort", "2181");

        //创建job，并设置主类
        Job job = Job.getInstance(config);
        job.setJarByClass(Test03Hbase2Hbase.class);

        // scan查询实例
        Scan scan = new Scan();
        // 添加查询的列，参数为列族、列
        scan.addColumn(Bytes.toBytes("info"), Bytes.toBytes("all"));
        // 设置job的读取输入的表，Mapper类，输出k,v类型
        TableMapReduceUtil.initTableMapperJob("cars_table", scan, Map.class, Text.class, Text.class, job);

        // 3 设置map和reduce类
        job.setMapperClass(Map.class);

        // 4 设置map输出
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);

        // 5 设置最终输出kv类型
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        // 6 设置输入和输出路径
        TableMapReduceUtil.initTableReducerJob("package_info", Reduce.class, job);
//        FileInputFormat.setInputPaths(job, new Path("input/"));

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