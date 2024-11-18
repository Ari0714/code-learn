package com.itbys._02_user_behaviors_analysis._02_NetWorkFlowAnalysis;

import com.itbys._02_user_behaviors_analysis.bean.UserBehaviors;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;

import java.time.Duration;

/**
 * Author xx
 * Date 2023/2/19
 * Desc 设置滚动时间窗口，实时统计每小时内的网站PV
 */
public class _02_pv {

    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        //设置时间类型
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

        DataStreamSource<String> fileDS = env.readTextFile("input/UserBehavior.csv");

        SingleOutputStreamOperator<UserBehaviors> mapDS = fileDS.map(new MapFunction<String, UserBehaviors>() {
            @Override
            public UserBehaviors map(String s) throws Exception {
                String[] strings = s.split(",");
                return new UserBehaviors(Long.parseLong(strings[0]), Long.parseLong(strings[1]), Integer.parseInt(strings[2]), strings[3], Long.parseLong(strings[4]));
            }
        });

        //设置watermark
        SingleOutputStreamOperator<UserBehaviors> watermarks = mapDS.assignTimestampsAndWatermarks(
                WatermarkStrategy.<UserBehaviors>forBoundedOutOfOrderness(Duration.ofSeconds(2))
                        .withTimestampAssigner(new SerializableTimestampAssigner<UserBehaviors>() {
                            @Override
                            public long extractTimestamp(UserBehaviors userBehaviors, long l) {
                                return userBehaviors.getTimestamp() * 1000L;
                            }
                        })
        );

        // 计算
        SingleOutputStreamOperator<Tuple2<String, Integer>> sumDS = watermarks
                .filter(x -> "pv".equals(x.getBehavior()))
                .map(new MapFunction<UserBehaviors, Tuple2<String, Integer>>() {
                    @Override
                    public Tuple2<String, Integer> map(UserBehaviors userBehaviors) throws Exception {
                        return new Tuple2<>("pv", 1);
                    }
                })
                .windowAll(TumblingEventTimeWindows.of(Time.hours(1)))
                .sum(1);
        sumDS.print();


        env.execute();

    }


}
