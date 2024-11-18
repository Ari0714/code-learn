package com.itbys.spark_basic.java.spark_core;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.util.Arrays;

/**
 * Author xx
 * Date 2022/3/30
 * Desc
 */
public class _01_spark_wc {

    public static void main(String[] args) {

        SparkConf conf = new SparkConf().setAppName(_01_spark_wc.class.getSimpleName()).setMaster("local[*]");
        JavaSparkContext jsc = new JavaSparkContext(conf);

        JavaRDD<Tuple2<String, Integer>> resRDD = jsc.textFile("03-spark-base/input/article.txt")
                .flatMap(x -> Arrays.asList(x.split(" ")).iterator())
                .mapToPair(x -> new Tuple2<>(x, 1))
                .reduceByKey((x, y) -> x + y)
                .map(x -> x)
                .sortBy(x -> x._2, false, 1);
        resRDD.foreach(x -> System.out.println(x));

    }

}
