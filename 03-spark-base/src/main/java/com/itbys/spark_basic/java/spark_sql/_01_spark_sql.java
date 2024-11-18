package com.itbys.spark_basic.java.spark_sql;

import com.itbys.spark_basic.java.bean.BaseLog;
import com.itbys.spark_basic.java.bean.Person;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.*;
import org.junit.Test;
import scala.Serializable;

/**
 * Author xx
 * Date 2022/3/30
 * Desc
 */
public class _01_spark_sql implements Serializable {

    /**
     * 使用SQLContext操作sql
     */
    @Test
    public void testSQLContext() throws AnalysisException {

        SparkConf conf = new SparkConf().setAppName(_01_spark_sql.class.getName()).setMaster("local[*]");
        JavaSparkContext jsc = new JavaSparkContext(conf);
        SQLContext sc = new SQLContext(jsc);

        //读取json文件
        Dataset<Row> json = sc.read().format("json").load("input/json.txt");
        Dataset<Row> json1 = sc.read().json("input/json.txt");
        Dataset<Row> json3 = sc.jsonFile("input/json.txt");
//        json3.show();

        //转化成RDD
        JavaRDD<Row> jsonRDD = json.javaRDD();

        //使用sql
        json.createTempView("json_info");
        sc.sql("select * from json_info").show();


        //RDD与df的相互转换
        JavaRDD<Person> personRDD = jsonRDD.map(new Function<Row, Person>() {
            @Override
            public Person call(Row row) throws Exception {
                return new Person(row.getLong(0) + "", row.getString(1));
            }
        });
    }

    /**
     * 使用SparkSession操作sql
     */
    @Test
    public void testSparkSession() throws AnalysisException {

        SparkConf conf = new SparkConf().setAppName(_01_spark_sql.class.getName()).setMaster("local[*]");
        SparkSession spark = SparkSession.builder().config(conf).getOrCreate();

        //读取json文件
        Dataset<Row> json = spark.read().format("json").load("input/json.txt");
        Dataset<Row> json1 = spark.read().json("input/json.txt");
//        json3.show();

        //转化成RDD
        JavaRDD<Row> jsonRDD = json.javaRDD();

        //使用sql
        json.createTempView("json_info");
        spark.sql("select * from json_info").show();

        //RDD与df的相互转换
        JavaRDD<BaseLog> personRDD = jsonRDD.map(new Function<Row, BaseLog>() {
            @Override
            public BaseLog call(Row row) throws Exception {
                return new BaseLog(row.getLong(0) + "");
            }
        });

        spark.createDataFrame(personRDD, BaseLog.class).show();

    }


    public static void main(String[] args) throws AnalysisException {

    }


}
