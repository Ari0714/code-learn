package com.itbys._02_user_behaviors_analysis._03_MarketAnalysis;

import com.itbys._02_user_behaviors_analysis.bean.ChannelPromotionCount;
import com.itbys._02_user_behaviors_analysis.bean.MarketingUserBehavior;
import com.itbys._02_user_behaviors_analysis.util.UserActivityLog;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;


/**
 * Author xx
 * Date 2023/2/19
 * Desc 不分渠道统计 behavior
 * 1、每5s统计过去1h
 * 2、过滤UNINSTALL
 */
public class _02_no_channel {

    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        //设置时间类型
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

        //input：
        DataStreamSource<MarketingUserBehavior> souDS = env.addSource(new UserActivityLog());

        //设置watermark
        SingleOutputStreamOperator<MarketingUserBehavior> watermarks = souDS.assignTimestampsAndWatermarks(
                new BoundedOutOfOrdernessTimestampExtractor<MarketingUserBehavior>(Time.seconds(2)) {
                    @Override
                    public long extractTimestamp(MarketingUserBehavior marketingUserBehavior) {
                        return marketingUserBehavior.getTimestamp();
                    }
                });
//        watermarks.print();

        SingleOutputStreamOperator<ChannelPromotionCount> aggreResult = watermarks
                .filter(x -> !x.getBehavior().contains("UNINSTALL"))
                .keyBy("behavior")
                .timeWindow(Time.hours(1), Time.seconds(5))
                .aggregate(new AggreFun(), new WinFun());
//        aggreResult.print();


        env.execute();

    }

    private static class AggreFun implements AggregateFunction<MarketingUserBehavior, Long, Long> {
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

    private static class WinFun implements WindowFunction<Long, ChannelPromotionCount, Tuple, TimeWindow> {
        @Override
        public void apply(Tuple tuple, TimeWindow timeWindow, Iterable<Long> iterable, Collector<ChannelPromotionCount> collector) throws Exception {

            String behavior = tuple.getField(0).toString();
            String timeStr = new java.sql.Time(timeWindow.getEnd()).toString();
            Long size = iterable.iterator().next();

            collector.collect(new ChannelPromotionCount("_no_channel", behavior, timeStr, size));

        }
    }
}
