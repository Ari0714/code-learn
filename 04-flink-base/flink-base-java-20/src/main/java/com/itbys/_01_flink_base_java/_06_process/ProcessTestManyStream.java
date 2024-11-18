package com.itbys._01_flink_base_java._06_process;

import com.itbys._01_flink_base_java.bean.Event;
import com.itbys._01_flink_base_java.bean.SensorReading;
import com.itbys._01_flink_base_java.util.TempSource;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.CoGroupFunction;
import org.apache.flink.api.common.functions.JoinFunction;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.api.java.tuple.Tuple4;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.streaming.api.functions.co.CoMapFunction;
import org.apache.flink.streaming.api.functions.co.CoProcessFunction;
import org.apache.flink.streaming.api.functions.co.ProcessJoinFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

import java.time.Duration;


/**
 * Author xx
 * Date 2023/2/19
 * Desc 多流转换
 */
public class ProcessTestManyStream {

    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        //设置时间类型
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

        DataStreamSource<SensorReading> mapDS = env.addSource(new TempSource());

        //设置watermark
        SingleOutputStreamOperator<SensorReading> watermarkDS = mapDS.assignTimestampsAndWatermarks(
                WatermarkStrategy.<SensorReading>forBoundedOutOfOrderness(Duration.ofSeconds(2))
                        .withTimestampAssigner(new SerializableTimestampAssigner<SensorReading>() {
                            @Override
                            public long extractTimestamp(SensorReading sensorReading, long l) {
                                return sensorReading.getTimestamp();
                            }
                        })
        );

        // 1.分流
        OutputTag<String> outputTag1 = new OutputTag<String>("ss1") {
        };
        OutputTag<String> outputTag2 = new OutputTag<String>("ss2") {
        };

        SingleOutputStreamOperator<String> processDS = watermarkDS
                .process(new ProcessFunction<SensorReading, String>() {
                    @Override
                    public void processElement(SensorReading sensorReading, Context context, Collector<String> collector) throws Exception {
                        if ("1".equals(sensorReading.getId())) {
                            context.output(outputTag1, sensorReading.toString());
                        } else if ("2".equals(sensorReading.getId())) {
                            context.output(outputTag2, sensorReading.toString());
                        } else
                            collector.collect(sensorReading.toString());
                    }
                });
//        processDS.print("main: ");
//        processDS.getSideOutput(outputTag1).print("side1");
//        processDS.getSideOutput(outputTag2).print("side2");

        // 2.合流： union（类型一致）, connect（类型不一致, 底层用的多）
        DataStreamSource<String> ds1 = env.fromElements("1", "2");
        DataStreamSource<String> ds2 = env.fromElements("1", "2");
        DataStream<String> unionDS = ds1.union(ds2);  // watermark以最小的为准，木桶原理

        // connect
        ds1.connect(ds2)
                .map(new CoMapFunction<String, String, String>() {
                    @Override
                    public String map1(String s) throws Exception {
                        return s;
                    }

                    @Override
                    public String map2(String s) throws Exception {
                        return s;
                    }
                });

//        env.execute();

//        testBillMatch();

        // 时间合流
//        testWindowJoin();
//        testIntervalJoin();
        testCoGroupjoin();

    }

    /**
     * @desc 间隔链接：coGroup join
     */
    public static void testCoGroupjoin() throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        //设置时间类型
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

        DataStream<Tuple2<String, Long>> stream1 = env
                .fromElements(
                        Tuple2.of("a", 1000L),
                        Tuple2.of("b", 1000L),
                        Tuple2.of("a", 2000L),
                        Tuple2.of("b", 2000L)
                )
                .assignTimestampsAndWatermarks(
                        WatermarkStrategy.<Tuple2<String, Long>>forMonotonousTimestamps()
                                .withTimestampAssigner(
                                        new SerializableTimestampAssigner<Tuple2<String, Long>>() {
                                            @Override
                                            public long extractTimestamp(Tuple2<String,
                                                    Long> stringLongTuple2, long l) {
                                                return stringLongTuple2.f1;
                                            }
                                        }
                                )
                );

        DataStream<Tuple2<String, Long>> stream2 = env
                .fromElements(
                        Tuple2.of("a", 3000L),
                        Tuple2.of("b", 3000L),
                        Tuple2.of("a", 4000L),
                        Tuple2.of("b", 4000L)
                )
                .assignTimestampsAndWatermarks(
                        WatermarkStrategy.<Tuple2<String, Long>>forMonotonousTimestamps()
                                .withTimestampAssigner(
                                        new SerializableTimestampAssigner<Tuple2<String, Long>>() {
                                            @Override
                                            public long extractTimestamp(Tuple2<String,
                                                    Long> stringLongTuple2, long l) {
                                                return stringLongTuple2.f1;
                                            }
                                        }
                                )
                );

        // 和window join类似，但是方法参数为聚合数据，且输出为collect，更加灵活
        DataStream<String> applyDS = stream1
                .coGroup(stream2)
                .where(x -> x.f0)
                .equalTo(x -> x.f0)
                .window(TumblingEventTimeWindows.of(Time.seconds(10)))
                .apply(new CoGroupFunction<Tuple2<String, Long>, Tuple2<String, Long>, String>() {
                    @Override
                    public void coGroup(Iterable<Tuple2<String, Long>> iterable, Iterable<Tuple2<String, Long>> iterable1, Collector<String> collector) throws Exception {
                        collector.collect(iterable.iterator().next().toString());
                    }
                });
        applyDS.print();


        env.execute();

    }


    /**
     * @desc 间隔链接：interval join
     */
    public static void testIntervalJoin() throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        //设置时间类型
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

        SingleOutputStreamOperator<Tuple3<String, String, Long>> orderStream = env
                .fromElements(
                        Tuple3.of("Mary", "order-1", 5000L),
                        Tuple3.of("Alice", "order-2", 5000L),
                        Tuple3.of("Bob", "order-3", 20000L),
                        Tuple3.of("Alice", "order-4", 20000L),
                        Tuple3.of("Cary", "order-5", 51000L)
                ).assignTimestampsAndWatermarks(WatermarkStrategy.<Tuple3<String, String, Long>>forMonotonousTimestamps()
                        .withTimestampAssigner(new SerializableTimestampAssigner<Tuple3<String, String, Long>>() {
                            @Override
                            public long extractTimestamp(Tuple3<String, String, Long>
                                                                 element, long recordTimestamp) {
                                return element.f2;
                            }
                        })
                );

        SingleOutputStreamOperator<Event> clickStream = env
                .fromElements(
                        new Event("Bob", "./cart", 2000L),
                        new Event("Alice", "./prod?id=100", 3000L),
                        new Event("Alice", "./prod?id=200", 3500L),
                        new Event("Bob", "./prod?id=2", 2500L),
                        new Event("Alice", "./prod?id=300", 36000L),
                        new Event("Bob", "./home", 30000L),
                        new Event("Bob", "./prod?id=1", 23000L),
                        new Event("Bob", "./prod?id=3", 33000L))
                .assignTimestampsAndWatermarks(WatermarkStrategy.<Event>forMonotonousTimestamps()
                        .withTimestampAssigner(new SerializableTimestampAssigner<Event>() {
                            @Override
                            public long extractTimestamp(Event element, long recordTimestamp) {
                                return element.getTimestamp();
                            }
                        })
                );

        SingleOutputStreamOperator<String> processDS = orderStream
                .keyBy(x -> x.f0)  //先分组
                .intervalJoin(clickStream.keyBy(x -> x.getName()))
                .between(Time.seconds(-10), Time.seconds(10))
                .process(new ProcessJoinFunction<Tuple3<String, String, Long>, Event, String>() {
                    @Override
                    public void processElement(Tuple3<String, String, Long> t3, Event event, Context context, Collector<String> collector) throws Exception {
                        collector.collect(t3 + ": " + event.toString());
                    }
                });
        processDS.print();


        env.execute();

    }


    /**
     * @desc 窗口链接：window join
     */
    public static void testWindowJoin() throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        //设置时间类型
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

        DataStream<Tuple2<String, Long>> stream1 = env
                .fromElements(
                        Tuple2.of("a", 1000L),
                        Tuple2.of("b", 1000L),
                        Tuple2.of("a", 2000L),
                        Tuple2.of("b", 2000L)
                )
                .assignTimestampsAndWatermarks(
                        WatermarkStrategy.<Tuple2<String, Long>>forMonotonousTimestamps()
                                .withTimestampAssigner(
                                        new SerializableTimestampAssigner<Tuple2<String, Long>>() {
                                            @Override
                                            public long extractTimestamp(Tuple2<String,
                                                    Long> stringLongTuple2, long l) {
                                                return stringLongTuple2.f1;
                                            }
                                        }
                                )
                );

        DataStream<Tuple2<String, Long>> stream2 = env
                .fromElements(
                        Tuple2.of("a", 3000L),
                        Tuple2.of("b", 3000L),
                        Tuple2.of("a", 4000L),
                        Tuple2.of("b", 4000L)
                )
                .assignTimestampsAndWatermarks(
                        WatermarkStrategy.<Tuple2<String, Long>>forMonotonousTimestamps()
                                .withTimestampAssigner(
                                        new SerializableTimestampAssigner<Tuple2<String, Long>>() {
                                            @Override
                                            public long extractTimestamp(Tuple2<String,
                                                    Long> stringLongTuple2, long l) {
                                                return stringLongTuple2.f1;
                                            }
                                        }
                                )
                );

        DataStream<String> applyDS = stream1
                .join(stream2)
                .where(x -> x.f0)
                .equalTo(x -> x.f0)
                .window(TumblingEventTimeWindows.of(Time.seconds(5)))
                .apply(new JoinFunction<Tuple2<String, Long>, Tuple2<String, Long>, String>() {
                    @Override
                    public String join(Tuple2<String, Long> t1, Tuple2<String, Long> t2) throws Exception {
                        return t1.toString() + t2.toString();
                    }
                });
        applyDS.print();


        env.execute();
    }


    /**
     * @desc 测试对账, CoProcessFunction使用
     */
    public static void testBillMatch() throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        //设置时间类型
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

        // 来自 app 的支付日志
        SingleOutputStreamOperator<Tuple3<String, String, Long>> appStream = env
                .fromElements(
                        Tuple3.of("order-1", "app", 1000L),
                        Tuple3.of("order-2", "app", 2000L),
                        Tuple3.of("order-3", "app", 3000L)
                ).assignTimestampsAndWatermarks(
                        WatermarkStrategy.<Tuple3<String, String, Long>>forMonotonousTimestamps()
                                .withTimestampAssigner(new SerializableTimestampAssigner<Tuple3<String, String, Long>>() {
                                    @Override
                                    public long extractTimestamp(Tuple3<String, String, Long> element, long recordTimestamp) {
                                        return element.f2;
                                    }
                                })
                );

        // 来自第三方支付平台的支付日志
        SingleOutputStreamOperator<Tuple4<String, String, String, Long>> thirdpartStream = env
                .fromElements(
                        Tuple4.of("order-1", "third-party", "success", 3000L),
                        Tuple4.of("order-3", "third-party", "success", 4000L)
                )
                .assignTimestampsAndWatermarks(WatermarkStrategy.<Tuple4<String, String, String, Long>>forMonotonousTimestamps()
                        .withTimestampAssigner(new SerializableTimestampAssigner<Tuple4<String, String, String, Long>>() {
                            @Override
                            public long extractTimestamp(Tuple4<String, String, String, Long> element, long recordTimestamp) {
                                return element.f3;
                            }
                        })
                );

        SingleOutputStreamOperator<String> processDS = appStream
                .connect(thirdpartStream)
                .keyBy(x -> x.f0, x -> x.f0)
                .process(new CoProcessFunction<Tuple3<String, String, Long>, Tuple4<String, String, String, Long>, String>() {

                    // 定义状态
                    ValueState<Tuple3<String, String, Long>> appVs;
                    ValueState<Tuple4<String, String, String, Long>> thirdpartVs;

                    @Override
                    public void open(Configuration parameters) throws Exception {
                        appVs = getRuntimeContext().getState(new ValueStateDescriptor<Tuple3<String, String, Long>>("app", Types.TUPLE(Types.STRING, Types.STRING, Types.LONG)));
                        thirdpartVs = getRuntimeContext().getState(new ValueStateDescriptor<Tuple4<String, String, String, Long>>("thirdpart", Types.TUPLE(Types.STRING, Types.STRING, Types.STRING, Types.LONG)));
                    }

                    @Override
                    public void processElement1(Tuple3<String, String, Long> t3, Context context, Collector<String> collector) throws Exception {
                        if (thirdpartVs.value() != null) {
                            collector.collect(t3 + ": " + thirdpartVs.value() + "对账成功");
                            thirdpartVs.clear();
                        } else {
                            appVs.update(t3);
                            context.timerService().registerEventTimeTimer(5000L);
                        }
                    }

                    @Override
                    public void processElement2(Tuple4<String, String, String, Long> t4, Context context, Collector<String> collector) throws Exception {
                        if (appVs.value() != null) {
                            collector.collect(appVs.value() + ": " + t4 + "对账成功");
                            appVs.clear();
                        } else {
                            thirdpartVs.update(t4);
                            context.timerService().registerEventTimeTimer(5000L);
                        }
                    }

                    @Override
                    public void onTimer(long timestamp, OnTimerContext ctx, Collector<String> out) throws Exception {
                        if (appVs.value() != null) {
                            out.collect(appVs.value() + ": 对账失败");
                        } else if (thirdpartVs.value() != null) {
                            out.collect(thirdpartVs.value() + ": 对账失败");
                        }

                        appVs.clear();
                        thirdpartVs.clear();
                    }
                });
        processDS.print("processDS: ");


        env.execute();

    }

}
