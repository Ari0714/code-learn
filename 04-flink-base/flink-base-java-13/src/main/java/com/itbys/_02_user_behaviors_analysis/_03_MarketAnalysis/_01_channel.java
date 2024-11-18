package com.itbys._02_user_behaviors_analysis._03_MarketAnalysis;

import com.itbys._02_user_behaviors_analysis.bean.ChannelPromotionCount;
import com.itbys._02_user_behaviors_analysis.bean.MarketingUserBehavior;
import com.itbys._02_user_behaviors_analysis.util.*;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.SlidingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.sql.Timestamp;


/**
 * Author xx
 * Date 2023/2/19
 * Desc 分渠道统计behavior
 * 1、每5s统计过去1h
 * 2、过滤UNINSTALL
 */
public class _01_channel {

    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        //设置时间类型
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

        DataStreamSource<MarketingUserBehavior> marketingDS = env.addSource(new UserActivityLog());

        //设置watermark
        SingleOutputStreamOperator<MarketingUserBehavior> watermarks = marketingDS.assignTimestampsAndWatermarks(
                new BoundedOutOfOrdernessTimestampExtractor<MarketingUserBehavior>(Time.seconds(2)) {
                    @Override
                    public long extractTimestamp(MarketingUserBehavior marketingUserBehavior) {
                        return marketingUserBehavior.getTimestamp();
                    }
                });

        // 计算
        SingleOutputStreamOperator<ChannelPromotionCount> aggrResult = watermarks
                .filter(x -> !x.getBehavior().contains("UNINSTALL"))
                .keyBy("behavior", "channel")
                .window(SlidingEventTimeWindows.of(Time.hours(1), Time.seconds(5)))
                .aggregate(new Aggr(), new WinFunc());
        aggrResult.print();


        env.execute();
    }


    private static class Aggr implements AggregateFunction<MarketingUserBehavior, Long, Long> {
        @Override
        public Long createAccumulator() {
            return 0L;
        }

        @Override
        public Long add(MarketingUserBehavior marketingUserBehavior, Long aLong) {
            return aLong + 1;
        }

        @Override
        public Long getResult(Long aLong) {
            return aLong;
        }

        @Override
        public Long merge(Long aLong, Long acc1) {
            return aLong + acc1;
        }
    }

    private static class WinFunc implements WindowFunction<Long, ChannelPromotionCount, Tuple, TimeWindow> {

        @Override
        public void apply(Tuple tuple, TimeWindow timeWindow, Iterable<Long> iterable, Collector<ChannelPromotionCount> collector) throws Exception {

            String behavior = tuple.getField(0).toString();
            String channel = tuple.getField(1).toString();

            Long cnt = iterable.iterator().next();
            String time = new Timestamp(timeWindow.getEnd()).toString();

            collector.collect(new ChannelPromotionCount(channel, behavior, time, cnt));

        }
    }
}
