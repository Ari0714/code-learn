package com.itbys._01_flink_base_java._03_transform;

import com.itbys._01_flink_base_java.bean.SensorReading;
import org.apache.flink.api.common.functions.*;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.*;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

import java.util.*;

/**
 * Author xx
 * Date 2023/2/18
 * Desc
 * 2023/2/18：split多流处理等函数被移除
 */
public class TransformTest {

    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        DataStreamSource<String> fileDS = env.readTextFile("04-flink-base/flink-base-java-13/input/sensor.txt");

        //1、map
        fileDS.map(x -> x + "_test");

        //2、flatmap
        fileDS.flatMap(new FlatMapFunction<String, String>() {
            @Override
            public void flatMap(String s, Collector<String> collector) throws Exception {
                String[] strings = s.split(" ");
                for (String string : strings) {
                    collector.collect(string);
                }
            }
        });

        //3、filter
        fileDS.filter(x -> x.contains("id"));


        /**
         * 聚合函数
         * keyBy
         *  max min（maxby minby）
         *  sum reduce
         */
        SingleOutputStreamOperator<SensorReading> mapDS = fileDS.map(new MapFunction<String, SensorReading>() {
            @Override
            public SensorReading map(String s) throws Exception {
                String[] strings = s.split(",");
                return new SensorReading(strings[0], Long.parseLong(strings[1]), Double.parseDouble(strings[2]));
            }
        });

        //4、keyBy
        KeyedStream<SensorReading, String> keyByDS = mapDS.keyBy(new KeySelector<SensorReading, String>() {
            @Override
            public String getKey(SensorReading sensorReading) throws Exception {
                return sensorReading.getId();
            }
        });

        //5、聚合算子sum max min（其他不变，只更新temp）,  maxBy minBy（最大temp的完整数据）
        SingleOutputStreamOperator<SensorReading> maxDS = keyByDS.max("temp");
        //滚动聚合，取当前最大的temp
        SingleOutputStreamOperator<SensorReading> maxByDS = keyByDS.maxBy("temp");
//        maxDS.print("max: ");
//        maxByDS.print("maxby: ");

        //reduce 取最新的时间戳 和 最大的温度值
        SingleOutputStreamOperator<SensorReading> reduceDS = keyByDS.reduce(new ReduceFunction<SensorReading>() {
            @Override
            public SensorReading reduce(SensorReading t1, SensorReading t2) throws Exception {
                return new SensorReading(t1.getId(), Math.max(t1.getTimestamp(), t2.getTimestamp()), Math.max(t1.getTemp(), t2.getTemp()));
            }
        });
//        reduceDS.print();


        /**
         * 富函数
         * 增加生命周期，runtimeContext进而获取state
         */
        fileDS.filter(new RichFilterFunction<String>() {

            ArrayList<String> list = null;

            @Override
            public void open(Configuration parameters) throws Exception {
                super.open(parameters);
                list = (ArrayList<String>) Arrays.asList("1", "2", "3");  // 类比jdbc数据
            }

            @Override
            public boolean filter(String s) throws Exception {
                return list.contains(s);
            }

            @Override
            protected Object clone() throws CloneNotSupportedException {
                return super.clone();
            }
        });


        env.execute();

    }

}
