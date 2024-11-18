package com.itbys._02_user_behaviors_analysis._02_NetWorkFlowAnalysis;

import com.itbys._02_user_behaviors_analysis.bean.ApacheLog;
import com.itbys._02_user_behaviors_analysis.bean.UrlCount;
import org.apache.commons.compress.utils.Lists;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.common.state.ListState;
import org.apache.flink.api.common.state.ListStateDescriptor;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.SlidingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;


/**
 * Author xx
 * Date 2023/2/19
 * Desc 每隔 5 秒，输出最近 10 分钟内访问量最多的前 N 个 URL
 */
public class _01_url {

    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        //设置时间类型
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

        DataStreamSource<String> fileDS = env.readTextFile("04-flink-base/flink-base-java-13/input/apache.log");

        ////91.177.205.119 - - 17/05/2015:10:05:34 +0000 GET /reset.css
        SingleOutputStreamOperator<ApacheLog> mapDS = fileDS.map(new RichMapFunction<String, ApacheLog>() {

            SimpleDateFormat format;

            @Override
            public void open(Configuration parameters) throws Exception {
                format = new SimpleDateFormat("dd/MM/yyyy:HH:mm:ss");
            }

            @Override
            public ApacheLog map(String s) throws Exception {
                String[] strings = s.split(" ");
                long ts = format.parse(strings[3]).getTime();

                return new ApacheLog(strings[0], null, ts, strings[5], strings[6]);
            }
        });

        //设置watermark
        SingleOutputStreamOperator<ApacheLog> watermarks = mapDS.assignTimestampsAndWatermarks(
                WatermarkStrategy.<ApacheLog>forBoundedOutOfOrderness(Duration.ofSeconds(2))
                        .withTimestampAssigner(new SerializableTimestampAssigner<ApacheLog>() {
                            @Override
                            public long extractTimestamp(ApacheLog apacheLog, long l) {
                                return apacheLog.getTimeStamp() * 1000L;
                            }
                        })
        );
//        watermarks.print();

        SingleOutputStreamOperator<UrlCount> urlAgg = watermarks
                .keyBy(x -> x.getUrl())
                .window(SlidingEventTimeWindows.of(Time.minutes(10), Time.seconds(5)))
                .aggregate(new LogCountAGG(), new LogWinFunc());
//        urlAgg.print();

        SingleOutputStreamOperator<String> processDS = urlAgg
                .keyBy(x -> x.getStartTime())
                .process(new LogKeyProFunc(3));
        processDS.print();


        env.execute();

    }

    private static class LogCountAGG implements AggregateFunction<ApacheLog, Long, Long> {
        @Override
        public Long createAccumulator() {
            return 0L;
        }

        @Override
        public Long add(ApacheLog apacheLog, Long aLong) {
            return aLong + 1;
        }

        @Override
        public Long getResult(Long aLong) {
            return aLong;
        }

        @Override
        public Long merge(Long aLong, Long acc1) {
            return acc1 + aLong;
        }
    }

    private static class LogWinFunc implements WindowFunction<Long, UrlCount, String, TimeWindow> {

        @Override
        public void apply(String string, TimeWindow timeWindow, Iterable<Long> iterable, Collector<UrlCount> collector) throws Exception {

            String url = string;
            long start = timeWindow.getStart();
            long end = timeWindow.getEnd();
            Long cnt = iterable.iterator().next();
            collector.collect(new UrlCount(url, start, end, cnt));

        }
    }

    private static class LogKeyProFunc extends KeyedProcessFunction<Long, UrlCount, String> {

        private Integer top;

        public LogKeyProFunc(Integer top) {
            this.top = top;
        }

        ListState<UrlCount> listState;

        @Override
        public void open(Configuration parameters) throws Exception {
            listState = getRuntimeContext().getListState(new ListStateDescriptor<UrlCount>("list", UrlCount.class));
        }

        @Override
        public void processElement(UrlCount urlCount, Context context, Collector<String> collector) throws Exception {

            listState.add(urlCount);
            context.timerService().registerEventTimeTimer(context.timestamp() + 1);

        }

        @Override
        public void onTimer(long timestamp, OnTimerContext ctx, Collector<String> out) throws Exception {

            ArrayList<UrlCount> urlCounts = Lists.newArrayList(listState.get().iterator());

            // 排序取出 top 3
            urlCounts.sort((x, y) -> y.getCnt().intValue() - x.getCnt().intValue());
            List<UrlCount> subList;
            if (urlCounts.size() >= 3)
                subList = urlCounts.subList(0, top);
            else
                subList = urlCounts;

            // 输出
            StringBuffer stringBuffer = new StringBuffer("时间：" + ctx.timestamp() + "\n");
            for (UrlCount urlCount : subList) {
                stringBuffer.append(urlCount.toString() + "\n");
            }
            stringBuffer.append("======\n");

            out.collect(stringBuffer.toString());
        }
    }
}
