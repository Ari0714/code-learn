package com.itbys._01_flink_base_java._05_window_watermark;

import com.itbys._01_flink_base_java.bean.SensorReading;
import com.itbys._01_flink_base_java.util.TempSource;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.OutputTag;

import java.time.Duration;

/**
 * Author Ari
 * Date 2023/2/18
 * Desc 演示window的其他可选api
 */
public class WindowTestOthApi {

    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

        DataStreamSource<String> fileDS = env.readTextFile("04-flink-base/flink-base-java-13/input/sensor.txt");
        SingleOutputStreamOperator<SensorReading> mapDS = fileDS.map(new MapFunction<String, SensorReading>() {
            @Override
            public SensorReading map(String s) throws Exception {
                String[] strings = s.split(",");
                return new SensorReading(strings[0], Long.parseLong(strings[1]), Double.parseDouble(strings[2]));
            }
        });

        DataStreamSource<SensorReading> sourceDS = env.addSource(new TempSource());

        // 新写法：乱序流
        SingleOutputStreamOperator<SensorReading> watermarks = sourceDS.assignTimestampsAndWatermarks(
                WatermarkStrategy.<SensorReading>forBoundedOutOfOrderness(Duration.ofSeconds(2))
                        .withTimestampAssigner(new SerializableTimestampAssigner<SensorReading>() {
                            @Override
                            public long extractTimestamp(SensorReading sensorReading, long l) {
                                return sensorReading.getTimestamp();
                            }
                        })
        );

        // 窗口定义
        watermarks.keyBy(new KeySelector<SensorReading, String>() {
            @Override
            public String getKey(SensorReading sensorReading) throws Exception {
                return sensorReading.getId();
            }
        })
                .window(TumblingEventTimeWindows.of(Time.seconds(2))); // 时间：滚动


        //1、触发器

        //2、移除器

        //1、侧输出流
        OutputTag<Tuple2<String, Double>> outputTag = new OutputTag<Tuple2<String, Double>>("late"){};

        SingleOutputStreamOperator<Tuple2<String, Double>> reduceDS = watermarks
                .map(new MapFunction<SensorReading, Tuple2<String, Double>>() {
                    @Override
                    public Tuple2<String, Double> map(SensorReading x) throws Exception {
                        return new Tuple2<String, Double>(x.getId(), x.getTemp());
                    }
                })
                .keyBy(new KeySelector<Tuple2<String, Double>, String>() {
                    @Override
                    public String getKey(Tuple2<String, Double> stringDoubleTuple2) throws Exception {
                        return stringDoubleTuple2.f0;
                    }
                })
                .windowAll(TumblingEventTimeWindows.of(Time.seconds(5)))
                .allowedLateness(Time.seconds(10))
                .sideOutputLateData(outputTag)
                .reduce(new ReduceFunction<Tuple2<String, Double>>() {
                    @Override
                    public Tuple2<String, Double> reduce(Tuple2<String, Double> t1, Tuple2<String, Double> t2) throws Exception {
                        return new Tuple2<String, Double>(t1.f0, t1.f1 + t2.f1);
                    }
                });
//        reduceDS.print("reduceDS: ");
//        reduceDS.getSideOutput(outputTag).print("outputTag: ");




        env.execute();

    }


}
