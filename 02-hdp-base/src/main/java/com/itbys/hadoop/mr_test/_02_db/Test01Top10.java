package com.itbys.hadoop.mr_test._02_db;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapred.lib.db.DBWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.db.DBConfiguration;
import org.apache.hadoop.mapreduce.lib.db.DBOutputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

/**
 * Author xx
 * Date 2022/7/19
 * Desc 折线：出行时间
 */
public class Test01Top10 {

    public static class ReceiveTable implements Writable, DBWritable {

        //定义字段
        private String columnA;
        private int sum;

        public ReceiveTable() {

        }

        public ReceiveTable(String columnA, int sum) {
            this.columnA = columnA;
            this.sum = sum;
        }

        @Override
        public void write(PreparedStatement statement) throws SQLException {
            statement.setString(1, this.columnA);
            statement.setInt(2, this.sum);
        }

        @Override
        public void readFields(ResultSet resultSet) throws SQLException {
            this.columnA = resultSet.getString(1);
            this.sum = resultSet.getInt(2);

        }

        @Override
        public void write(DataOutput out) throws IOException {
        }

        @Override
        public void readFields(DataInput in) throws IOException {
        }

    }

    public static class Map extends Mapper<LongWritable, Text, Text, Text> {

        Text k = new Text();
        Text v = new Text();

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

            String s = value.toString();

            //不同站点发送到不同reduce key
            if (s.trim().length() > 0 && s.contains("deal_date")) {

                //获取小时
                JSONObject jsonObject = JSON.parseObject(s);
                String hourr = jsonObject.getString("deal_date").split(" ")[1].split(":")[0];

                k.set("1");
                v.set(hourr);
                context.write(k, v);
            }
        }
    }


    public static class Reduce extends Reducer<Text, Text, ReceiveTable, Text> {

        HashMap<String, Integer> hashMap = new HashMap<>();

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
                    return Integer.parseInt(o1.getKey()) - Integer.parseInt(o2.getKey());
                }
            });

            //写出
            for (java.util.Map.Entry<String, Integer> entry : entries) {
                ReceiveTable receiveTable = new ReceiveTable(entry.getKey(), entry.getValue());
                context.write(receiveTable, null);
            }

        }
    }


    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {

        String driverClass = "com.mysql.jdbc.Driver";
        String dbUrl = "jdbc:mysql://hdp:3306/travel?useUnicode=true&characterEncoding=utf8";
        String userName = "root";
        String passwd = "111111";
        String tableName = "travel_time";
        String[] fields = {"hourr", "cnt"};

        Configuration conf = new Configuration();
        DBConfiguration.configureDB(conf, driverClass, dbUrl, userName, passwd);

        Job job = Job.getInstance(conf);
        job.setJarByClass(Test01Top10.class);
        job.setMapOutputValueClass(Text.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapperClass(Map.class);
        job.setReducerClass(Reduce.class);

        FileInputFormat.setInputPaths(job, new Path("input/data_format.txt"));
        DBOutputFormat.setOutput(job, tableName, fields);

        boolean b = job.waitForCompletion(true);
        System.exit(b ? 0 : 1);

    }


}
