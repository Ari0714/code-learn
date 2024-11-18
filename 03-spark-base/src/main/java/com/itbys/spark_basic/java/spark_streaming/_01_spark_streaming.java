package com.itbys.spark_basic.java.spark_streaming;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.AnalysisException;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.junit.Test;
import scala.Serializable;
import scala.Tuple2;

import java.util.Arrays;
import java.util.Iterator;

/**
 * Author xx
 * Date 2022/3/30
 * Desc
 */
public class _01_spark_streaming implements Serializable {

    /**
     * 使用JavaStreamingContext
     */
    @Test
    public void testJavaStreamingContext() throws AnalysisException, InterruptedException {

        SparkConf conf = new SparkConf().setAppName(_01_spark_streaming.class.getName()).setMaster("local[*]");
        JavaStreamingContext jsc = new JavaStreamingContext(conf, Durations.seconds(5));

        JavaDStream<Tuple2<String, Integer>> transformDS = jsc.socketTextStream("hdp", 9999)
                .flatMap(new FlatMapFunction<String, String>() {
                    @Override
                    public Iterator<String> call(String s) throws Exception {
                        return Arrays.asList(s.split(" ")).iterator();
                    }
                })
                .mapToPair(x -> new Tuple2<>(x, 1))
                .reduceByKey((x, y) -> x + y)
                .transform(new Function<JavaPairRDD<String, Integer>, JavaRDD<Tuple2<String, Integer>>>() {
                    @Override
                    public JavaRDD<Tuple2<String, Integer>> call(JavaPairRDD<String, Integer> stringIntegerJavaPairRDD) throws Exception {
                        return stringIntegerJavaPairRDD.map(x -> x).sortBy(x -> x._2, false, 1);
                    }
                });
        transformDS.print();

        jsc.start();
        jsc.awaitTermination();

    }


    public static void main(String[] args) throws AnalysisException, InterruptedException {


    }

}
