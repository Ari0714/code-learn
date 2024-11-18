package com.itbys._02_user_behaviors_analysis._03_MarketAnalysis;

import com.itbys._02_user_behaviors_analysis.bean.AdClickEvent;
import com.itbys._02_user_behaviors_analysis.bean.AdCountViewByProvince;
import com.itbys._02_user_behaviors_analysis.bean.BlackListUserWarning;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

/**
 * Author xx
 * Date 2023/2/19
 * Desc 在上一步的基础上 将同一用户一天点击某一广告超过100次加入黑名单
 */
public class _04_adStatisticsFilter {

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

        //定义过程函数，过滤
        SingleOutputStreamOperator<AdClickEvent> processDS = watermarksDS
                .keyBy("userId", "adId")
                .process(new KeyProcess(100));

        //统计结果
        SingleOutputStreamOperator<AdCountViewByProvince> aggreResult = watermarksDS
                .keyBy("province")
                .window(TumblingEventTimeWindows.of(Time.hours(1), Time.seconds(5)))
                .aggregate(new AggreFun(), new AdWinFun());
//        aggreResult.print();
        processDS.getSideOutput(new OutputTag<BlackListUserWarning>("new_black_user") {
        }).print();


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

    private static class KeyProcess extends KeyedProcessFunction<Tuple, AdClickEvent, AdClickEvent> {

        int clickThreshold;

        public KeyProcess(int clickThreshold) {
            this.clickThreshold = clickThreshold;
        }

        //定义点击次数
        ValueState<Long> clickCntState;
        //定义是否已经加入黑名单
        ValueState<Boolean> isBlackState;

        @Override
        public void open(Configuration parameters) throws Exception {
            clickCntState = getRuntimeContext().getState(new ValueStateDescriptor<Long>("clickCntState", Long.class, 0L));
            isBlackState = getRuntimeContext().getState(new ValueStateDescriptor<Boolean>("isBlackState", Boolean.class, false));
        }

        @Override
        public void processElement(AdClickEvent adClickEvent, Context context, Collector<AdClickEvent> collector) throws Exception {

            Long curClickCnt = clickCntState.value();

            //注册定时器：明天
            if (curClickCnt == 0) {
                long tomorrowTS = (context.timerService().currentProcessingTime() / 24 * 60 * 60 * 1000L + 1) * (24 * 60 * 60 * 1000L);
                context.timerService().registerProcessingTimeTimer(tomorrowTS);
            }

            if (curClickCnt >= clickThreshold) {
                if (!isBlackState.value()) {
                    context.output(new OutputTag<BlackListUserWarning>("new_black_user") {},
                            new BlackListUserWarning(adClickEvent.getUserId(), adClickEvent.getAdId(), "warnning"));
                }
                return;
            }

            clickCntState.update(curClickCnt + 1);
            collector.collect(adClickEvent);

        }

        @Override
        public void onTimer(long timestamp, OnTimerContext ctx, Collector<AdClickEvent> out) throws Exception {
            clickCntState.clear();
            isBlackState.clear();
        }
    }
}
