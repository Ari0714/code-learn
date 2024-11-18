package com.itbys.spark_basic.java.spark_streaming;

import com.itbys.spark_basic.java.bean.BaseLog;
import com.itbys.spark_basic.java.util.MyKafkaUtil;
import com.itbys.spark_basic.java.util.MyMySQLUtil;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.Optional;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import scala.Tuple2;

import java.util.List;

/**
 * Author xx
 * Date 2023/12/23
 * Desc
 */
public class StreamingAnalysis {

    public static void main(String[] args) throws InterruptedException {

        SparkConf conf = new SparkConf().setAppName(StreamingAnalysis.class.getName()).setMaster("local[*]");
        JavaStreamingContext jsc = new JavaStreamingContext(conf, Durations.seconds(5));
        SparkSession spark = SparkSession.builder().config(conf).getOrCreate();
        jsc.checkpoint("ck");

        String topic = "ratings_topic";
        String groupId = "consumer_01";

        JavaInputDStream<ConsumerRecord<Object, Object>> inputKafkaDS = MyKafkaUtil.getKafkaStream(jsc, topic, groupId);

        //获取每个评分
        JavaDStream<String> modelDS = inputKafkaDS.map(x -> x.value().toString())
                .filter(x -> x.trim().length() > 0 && !x.contains("userId"))
                .map(x -> x.split(",")[2]);

        //累计统计
        JavaPairDStream<String, Integer> resultDS = modelDS.mapToPair(x -> new Tuple2<>(x, 1))
                .updateStateByKey(new Function2<List<Integer>, Optional<Integer>, Optional<Integer>>() {
                    @Override
                    public Optional<Integer> call(List<Integer> values, Optional<Integer> state) throws Exception {
                        // Optional其实有两个子类,一个子类是Some,一个子类是None
                        // 就是key有可能之前从来都没有出现过,意味着之前从来没有更新过状态
                        Integer newValue = 0;
                        if (state.isPresent()) {
                            newValue = state.get();
                        }
                        for (Integer value : values) {
                            newValue += value;
                        }

                        return Optional.of(newValue);
                    }
                });
        resultDS.print();


        //保存到mysql
        resultDS.foreachRDD(new VoidFunction<JavaPairRDD<String, Integer>>() {
            @Override
            public void call(JavaPairRDD<String, Integer> stringIntegerJavaPairRDD) throws Exception {

                JavaRDD<BaseLog> baseLogJavaRDD = stringIntegerJavaPairRDD.map(x -> new BaseLog(x._1));
                Dataset<Row> df = spark.createDataFrame(baseLogJavaRDD, BaseLog.class);

                MyMySQLUtil.saveToMysql(df, "rating_cnt");
            }
        });


        jsc.start();
        jsc.awaitTermination();

    }

}
