package com.itbys.spark_basic.java.util;


import com.itbys.spark_basic.java.common.CommonConfig;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.*;

/**
 * Author Ari
 * Date 2022/4/11
 * Desc
 */
public class MyMySQLUtil {


    /**
     * @desc 从mysql读取数据
     * @param: sparkSession
     * @param: table
     * @return: org.apache.spark.sql.Dataset<org.apache.spark.sql.Row>
     */
    public static Dataset<Row> readFromMysql(SparkSession sparkSession, String table) {
        return sparkSession
                .read()
                .format("jdbc")
                .option("driver", "org.postgresql.Driver")
                .option("url", "jdbc:mysql://hdp:3306/test?characterEncoding=utf-8&useSSL=false")
                .option("user", "root")
                .option("password", "111111")
                .option("dbtable", table)
                .load();

    }


    /**
     * @desc 往mysql写入数据
     * @param: dataset
     * @param: table
     * @param: saveMode
     */
    public static void saveToMysql(Dataset<Row> dataset, String table) {
        dataset.write()
                .format("jdbc")
                .option("url", "jdbc:mysql://hdp:3306/test?characterEncoding=utf-8&useSSL=false")
                .option("user", "root")
                .option("password", "111111")
                .option("dbtable", table)
                .mode(SaveMode.Overwrite)
                .save();
    }


    public static void main(String[] args) throws AnalysisException {

        SparkConf conf = new SparkConf().setAppName(MyMySQLUtil.class.getName()).setMaster("local[*]");
        SparkSession spark = SparkSession.builder().config(conf).getOrCreate();

        Dataset<Row> dataset = readFromMysql(spark, "qw_statistics_total_all");
        dataset.createTempView("qw_statistics_total_all");
        JavaRDD<Row> rowJavaRDD = spark.sql("select * from qw_statistics_total_all order by create_time desc").toJavaRDD();

        rowJavaRDD.map(new Function<Row, Long>() {
            @Override
            public Long call(Row row) throws Exception {
                return row.getLong(0);
            }
        })
                .foreach(x -> System.out.println(x));

    }

}
