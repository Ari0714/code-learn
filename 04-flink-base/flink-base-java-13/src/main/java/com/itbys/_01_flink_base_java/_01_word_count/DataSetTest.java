package com.itbys._01_flink_base_java._01_word_count;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.AggregateOperator;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;

/**
 * Author xx
 * Date 2023/2/18
 * Desc dataset api从2.12开始被整合，弃用
 * 1、可以使用流编码：提交任务时指定runtime-mode = BATCH
 */
public class DataSetTest {

    public static void main(String[] args) throws Exception {

        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        AggregateOperator<Tuple2<String, Integer>> sum = env.readTextFile("04-flink-base/flink-base-java-13/input/words.txt")
                .flatMap(new FlatMapFunction<String, Tuple2<String, Integer>>() {
                    @Override
                    public void flatMap(String s, Collector<Tuple2<String, Integer>> collector) throws Exception {
                        String[] s1 = s.split(" ");
                        for (String s2 : s1) {
                            collector.collect(new Tuple2<>(s2, 1));
                        }
                    }
                })
                .groupBy(0)
                .sum(1);
        sum.print();


//        env.execute();

    }

}
