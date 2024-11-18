package com.itbys.hadoop.mr_test._03_hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapred.lib.db.DBWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.db.DBConfiguration;
import org.apache.hadoop.mapreduce.lib.db.DBOutputFormat;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Test01Hbase2Db {

    public static class ReceiveTable implements Writable, DBWritable {

        //定义字段
        private String columnA;
        private String sum;

        public ReceiveTable() {

        }

        public ReceiveTable(String columnA, String sum) {
            this.columnA = columnA;
            this.sum = sum;
        }

        @Override
        public void write(PreparedStatement statement) throws SQLException {
            statement.setString(1, this.columnA);
            statement.setString(2, this.sum);
        }

        @Override
        public void readFields(ResultSet resultSet) throws SQLException {
            this.columnA = resultSet.getString(1);
            this.sum = resultSet.getString(2);

        }

        @Override
        public void write(DataOutput out) throws IOException {
            out.write(Bytes.toBytes(sum));
            Text.writeString(out, this.columnA);
        }

        @Override
        public void readFields(DataInput in) throws IOException {
            this.columnA = in.readUTF();
            this.sum = in.readUTF();

        }

    }

    public static class ReadHBaseMapper extends TableMapper<Text, Text> {
        @Override
        protected void map(ImmutableBytesWritable key, Result value, Mapper<ImmutableBytesWritable, Result, Text, Text>.Context context) throws IOException, InterruptedException {

            // input: 排名	车名	品牌	车型	评分	评价1	评价2	评价3
            String clazz = Bytes.toString(value.getValue("info".getBytes(), "all".getBytes()));

            if (!clazz.contains("评分")) {
                String[] strings = clazz.split("\t");
                context.write(new Text(strings[2]), new Text(strings[4].replace("分", "")));
            }

        }
    }

    public static class MyReducer extends Reducer<Text, Text, ReceiveTable, Text> {
        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {

            double sumScore = 0D;
            int cntScore = 0;

            for (Text value : values) {
                double score = Double.parseDouble(value.toString());
                sumScore += score;
                cntScore += 1;
            }

            double avgScore = sumScore / cntScore;

            ReceiveTable receiveTable = new ReceiveTable(key.toString(), avgScore + "");
            context.write(receiveTable, null);

        }
    }


    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {

        // 获取配置，并设置hbase的zk地址
        Configuration conf = new Configuration();
        conf.set("hbase.zookeeper.quorum", "hdp");

        // mysql 配置
        String driverClass = "com.mysql.jdbc.Driver";
        String dbUrl = "jdbc:mysql://hdp:3306/travel?useUnicode=true&characterEncoding=utf8";
        String userName = "root";
        String passwd = "111111";
        String tableName = "brand_rating";
        String[] fields = {"brand", "rating"};
        DBConfiguration.configureDB(conf, driverClass, dbUrl, userName, passwd);

        // 创建job，并设置主类
        Job job = Job.getInstance(conf);
        job.setJarByClass(Test01Hbase2Db.class);

        // scan查询实例
        Scan scan = new Scan();
        // 添加查询的列，参数为列族、列
        scan.addColumn(Bytes.toBytes("info"), Bytes.toBytes("all"));
        // 设置job的读取输入的表，Mapper类，输出k,v类型
        TableMapReduceUtil.initTableMapperJob("cars_table", scan, ReadHBaseMapper.class, Text.class, Text.class, job);

        // 设置reduce的类
        job.setReducerClass(MyReducer.class);
        // 设置输出格式是DataBase
        job.setOutputFormatClass(DBOutputFormat.class);
        // 设置输出时的k,v类型
        job.setOutputKeyClass(ReceiveTable.class);

        DBOutputFormat.setOutput(job, tableName, fields);

        // 执行job
        job.waitForCompletion(true);

    }


}


