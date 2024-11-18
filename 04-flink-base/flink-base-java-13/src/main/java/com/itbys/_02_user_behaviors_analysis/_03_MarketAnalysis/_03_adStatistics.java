package com.itbys._02_user_behaviors_analysis._03_MarketAnalysis;

import com.itbys._02_user_behaviors_analysis.bean.AdClickEvent;
import com.itbys._02_user_behaviors_analysis.bean.AdCountViewByProvince;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

/**
 * Author xx
 * Date 2023/2/19
 * Desc 省份分组
 * 1、然后开一小时的时间窗口，滑动距离为 5 秒，统计窗口内的点击事件数量
 */
public class _03_adStatistics {

    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        //设置时间类型
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

        //input：2315,36237,zhejiang,hangzhou,1511661641
        SingleOutputStreamOperator<AdClickEvent> watermarksDS = env.readTextFile("input/AdClickLog.csv")
                .map(new MapFunction<String, AdClickEvent>() {
                    @Override
                    public AdClickEvent map(String s) throws Exception {
                        String[] strings = s.split(",");
                        return new AdClickEvent(
                                Long.parseLong(strings[0]), Long.parseLong(strings[1]), strings[2], strings[3], Long.parseLong(strings[4]) * 1000L);
                    }
                })
                .assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor<AdClickEvent>(Time.seconds(2)) {
                    @Override
                    public long extractTimestamp(AdClickEvent adClickEvent) {
                        return adClickEvent.getTimestamp();
                    }
                });

        SingleOutputStreamOperator<AdCountViewByProvince> aggreResult = watermarksDS
                .keyBy("province")
                .window(TumblingEventTimeWindows.of(Time.hours(1), Time.seconds(5)))
                .aggregate(new AggreFun(), new AdWinFun());
        aggreResult.print();


        env.execute();

    }

    private static class AggreFun implements AggregateFunction<AdClickEvent, Long, Long> {
        @Override
        public Long createAccumulator() {
            return 0L;
        }

        @Override
        public Long add(AdClickEvent adClickEvent, Long aLong) {
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

    private static class AdWinFun implements WindowFunction<Long, AdCountViewByProvince, Tuple, TimeWindow> {
        @Override
        public void apply(Tuple tuple, TimeWindow timeWindow, Iterable<Long> iterable, Collector<AdCountViewByProvince> collector) throws Exception {
            String province = tuple.getField(0).toString();
            String timeStr = new java.sql.Time(timeWindow.getEnd()).toString();
            Long size = iterable.iterator().next();

            collector.collect(new AdCountViewByProvince(province, timeStr, size));
        }
    }
}


