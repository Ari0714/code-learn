package com.itbys._02_user_behaviors_analysis._01_HotItemsAnalysis;

import com.google.common.collect.Lists;
import com.itbys._02_user_behaviors_analysis.bean.UserBehaviors;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.state.ListState;
import org.apache.flink.api.common.state.ListStateDescriptor;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.streaming.api.functions.windowing.ProcessAllWindowFunction;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.SlidingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.time.Duration;
import java.util.*;


/**
 * Author xx
 * Date 2023/2/19
 * Desc 每隔 5 分钟输出最近一小时内点击量最多的前 N 个商品
 */
public class HotItemsAnalysis {

    public static void main(String[] args) throws Exception {

//        testHotItemsAnalysisWithWindowAll();
        testHotItemsAnalysisWithAgg();

    }


    /**
     * @desc 使用全窗口实现
     */
    public static void testHotItemsAnalysisWithWindowAll() throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        //设置时间类型
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

        DataStreamSource<String> fileDS = env.readTextFile("04-flink-base/flink-base-java-13/input/UserBehavior.csv");

        SingleOutputStreamOperator<UserBehaviors> mapDS = fileDS.map(new MapFunction<String, UserBehaviors>() {
            @Override
            public UserBehaviors map(String s) throws Exception {
                String[] strings = s.split(",");
                return new UserBehaviors(Long.parseLong(strings[0]), Long.parseLong(strings[1]), Integer.parseInt(strings[2]), strings[3], Long.parseLong(strings[4]));
            }
        });

        //设置watermark
        SingleOutputStreamOperator<UserBehaviors> watermarkDS = mapDS.assignTimestampsAndWatermarks(
                WatermarkStrategy.<UserBehaviors>forBoundedOutOfOrderness(Duration.ofSeconds(2))
                        .withTimestampAssigner(new SerializableTimestampAssigner<UserBehaviors>() {
                            @Override
                            public long extractTimestamp(UserBehaviors userBehaviors, long l) {
                                return userBehaviors.getTimestamp();
                            }
                        })
        );

        // 实现
        SingleOutputStreamOperator<String> processDS = watermarkDS
                .filter(x -> "pv".equals(x.getBehavior()))
                .windowAll(SlidingEventTimeWindows.of(Time.hours(1), Time.minutes(5)))
                .process(new ProcessAllWindowFunction<UserBehaviors, String, TimeWindow>() {
                    @Override
                    public void process(Context context, Iterable<UserBehaviors> iterable, Collector<String> collector) throws Exception {
                        HashMap<String, Integer> hashMap = new HashMap<>();

                        // 累加各个item的pv
                        ArrayList<UserBehaviors> list = Lists.newArrayList(iterable);
                        for (UserBehaviors userBehaviors : list) {
                            String itemId = userBehaviors.getItemId() + "";
                            if (hashMap.containsKey(itemId))
                                hashMap.put(itemId, hashMap.get(itemId) + 1);
                            else
                                hashMap.put(itemId, 1);
                        }

                        // 排序 top3
                        ArrayList<Map.Entry<String, Integer>> entries = new ArrayList<>(hashMap.entrySet());
                        entries.sort(new Comparator<Map.Entry<String, Integer>>() {
                            @Override
                            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                                return o2.getValue() - o1.getValue();
                            }
                        });
                        List<Map.Entry<String, Integer>> subList = entries.subList(0, 3);

                        // 输出
                        StringBuffer stringBuffer = new StringBuffer("=======\n");
                        for (Map.Entry<String, Integer> entry : subList) {
                            stringBuffer.append(entry.getKey() + ":" + entry.getValue() + "\n");
                        }
                        stringBuffer.append("=======\n");
                        collector.collect(stringBuffer.toString());

                    }

                });
        processDS.print();


        env.execute();

    }


    /**
     * @desc 使用增量窗口实现
     */
    public static void testHotItemsAnalysisWithAgg() throws Exception {

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
        SingleOutputStreamOperator<UserBehaviors> watermarkDS = mapDS.assignTimestampsAndWatermarks(
                WatermarkStrategy.<UserBehaviors>forBoundedOutOfOrderness(Duration.ofSeconds(2))
                        .withTimestampAssigner(new SerializableTimestampAssigner<UserBehaviors>() {
                            @Override
                            public long extractTimestamp(UserBehaviors userBehaviors, long l) {
                                return userBehaviors.getTimestamp();
                            }
                        })
        );

        // 增量计算
        SingleOutputStreamOperator<Tuple3<String, String, Integer>> aggregateDS = watermarkDS
                .filter(x -> "pv".equals(x.getBehavior()))
                .keyBy(x -> x.getItemId())
                .window(SlidingEventTimeWindows.of(Time.hours(1), Time.minutes(5)))
                .aggregate(new AggregateFunction<UserBehaviors, Tuple2<String, Integer>, Tuple2<String, Integer>>() {
                               @Override
                               public Tuple2<String, Integer> createAccumulator() {
                                   return new Tuple2<>(null, 0);
                               }

                               @Override
                               public Tuple2<String, Integer> add(UserBehaviors userBehaviors, Tuple2<String, Integer> t2) {
                                   return new Tuple2<>(userBehaviors.getItemId() + "", t2.f1 + 1);
                               }

                               @Override
                               public Tuple2<String, Integer> getResult(Tuple2<String, Integer> t2) {
                                   return new Tuple2<>(t2.f0, t2.f1);
                               }

                               @Override
                               public Tuple2<String, Integer> merge(Tuple2<String, Integer> stringIntegerTuple2, Tuple2<String, Integer> acc1) {
                                   return null;
                               }
                           },
                        new WindowFunction<Tuple2<String, Integer>, Tuple3<String, String, Integer>, Long, TimeWindow>() {
                            @Override
                            public void apply(Long aLong, TimeWindow timeWindow, Iterable<Tuple2<String, Integer>> iterable, Collector<Tuple3<String, String, Integer>> collector) throws Exception {

                                long start = timeWindow.getStart();
                                Tuple2<String, Integer> next = iterable.iterator().next();
                                collector.collect(new Tuple3<>(start + "", next.f0, next.f1));
                            }
                        }
                );
//        aggregateDS.print();

        // 取出top3
        SingleOutputStreamOperator<String> processDS = aggregateDS
                .keyBy(x -> x.f0)
                .process(new KeyedProcessFunction<String, Tuple3<String, String, Integer>, String>() {
                    ListState<Tuple3<String, String, Integer>> itemCntLs = null;

                    @Override
                    public void open(Configuration parameters) throws Exception {
                        itemCntLs = getRuntimeContext().getListState(new ListStateDescriptor<Tuple3<String, String, Integer>>("item-cnt", Types.TUPLE(Types.STRING, Types.STRING, Types.INT)));
                    }

                    @Override
                    public void processElement(Tuple3<String, String, Integer> t3, Context context, Collector<String> collector) throws Exception {
                        itemCntLs.add(t3);
                        context.timerService().registerEventTimeTimer(1L);

                    }

                    @Override
                    public void onTimer(long timestamp, OnTimerContext ctx, Collector<String> out) throws Exception {

                        ArrayList<Tuple3<String, String, Integer>> tuple3s = Lists.newArrayList(itemCntLs.get());

                        // 排序 top n
                        tuple3s.sort(new Comparator<Tuple3<String, String, Integer>>() {
                            @Override
                            public int compare(Tuple3<String, String, Integer> o1, Tuple3<String, String, Integer> o2) {
                                return o2.f2 - o1.f2;
                            }
                        });
                        List<Tuple3<String, String, Integer>> subList = tuple3s.subList(0, 3);

                        // 输出
                        StringBuffer stringBuffer = new StringBuffer("=======\n");
                        for (Tuple3<String, String, Integer> t3 : subList) {
                            stringBuffer.append(t3 + "\n");
                        }
                        stringBuffer.append("=======\n");
                        out.collect(stringBuffer.toString());

                    }
                });
        processDS.print();


        env.execute();

    }

}
