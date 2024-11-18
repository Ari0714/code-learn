package com.itbys._01_flink_base_java._05_window_watermark;

import com.google.common.collect.Lists;
import com.itbys._01_flink_base_java.bean.SensorReading;
import com.itbys._01_flink_base_java.util.TempSource;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.time.Duration;
import java.util.ArrayList;

/**
 * Author xx
 * Date 2023/2/18
 * Desc
 */
public class WindowTest {

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

        // 设置watermark：过时写法
//        SingleOutputStreamOperator<SensorReading> watermarks = mapDS
//                .assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor<SensorReading>(Time.seconds(2)) {
//                    @Override
//                    public long extractTimestamp(SensorReading sensorReading) {
//                        return sensorReading.getTimestamp() * 1000L;
//                    }
//                });

        // 新写法：有序流
//        SingleOutputStreamOperator<SensorReading> watermarks = mapDS.assignTimestampsAndWatermarks(
//                WatermarkStrategy.<SensorReading>forMonotonousTimestamps()
//                        .withTimestampAssigner(new SerializableTimestampAssigner<SensorReading>() {
//                            @Override
//                            public long extractTimestamp(SensorReading sensorReading, long l) {
//                                return sensorReading.getTimestamp() * 1000L;
//                            }
//                        })
//        );

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
//                .window(SlidingEventTimeWindows.of(Time.seconds(60), Time.seconds(2))) // 时间：滑动
//                .window(EventTimeSessionWindows.withGap(Time.seconds(30))) // 时间：会话
//                .countWindow(10) // 计数：滚动
//                .countWindow(60, 10) // 计数：滑动
//                .windowAll(TumblingEventTimeWindows.of(Time.seconds(20)))


        //1、增量窗口函数reduce：计算窗口的每个传感器温度总和
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
                .window(TumblingEventTimeWindows.of(Time.seconds(5)))
                .reduce(new ReduceFunction<Tuple2<String, Double>>() {
                    @Override
                    public Tuple2<String, Double> reduce(Tuple2<String, Double> t1, Tuple2<String, Double> t2) throws Exception {
                        return new Tuple2<String, Double>(t1.f0, t1.f1 + t2.f1);
                    }
                });
//        reduceDS.print("reduceDS: ");

        //1、增量窗口函数aggregate：计算窗口的每个传感器平均温度
        SingleOutputStreamOperator<Tuple2<String, Double>> aggregateDS = watermarks
                .keyBy(new KeySelector<SensorReading, String>() {
                    @Override
                    public String getKey(SensorReading sensorReading) throws Exception {
                        return sensorReading.getId();
                    }
                })
                .window(TumblingEventTimeWindows.of(Time.seconds(5)))
                .aggregate(new AggregateFunction<SensorReading, Tuple3<String, Double, Integer>, Tuple2<String, Double>>() {
                    @Override
                    public Tuple3<String, Double, Integer> createAccumulator() {
                        return new Tuple3<>(null, 0d, 0);
                    }

                    @Override  // 来一条数据计算一次
                    public Tuple3<String, Double, Integer> add(SensorReading sensorReading, Tuple3<String, Double, Integer> t2) {
                        return new Tuple3<>(sensorReading.getId(), t2.f1 + sensorReading.getTemp(), t2.f2 + 1);
                    }

                    @Override  // 最后窗口关闭计算，只计算一次
                    public Tuple2<String, Double> getResult(Tuple3<String, Double, Integer> t2) {
                        return new Tuple2<>(t2.f0, t2.f1 / t2.f2); // 结果从累加器拿到
                    }

                    @Override // 窗口关闭计算，一般用于会话窗口
                    public Tuple3<String, Double, Integer> merge(Tuple3<String, Double, Integer> t1, Tuple3<String, Double, Integer> t2) {
                        return new Tuple3<>(t1.f0, t1.f1 + t2.f1, t1.f2 + t2.f2);
                    }
                });
//        aggregateDS.print();


        //2、全窗口函数 apply(windowFunction(in、out、key、window))：计算窗口的每个传感器温度总和
        SingleOutputStreamOperator<Tuple2<String, Double>> applyDS = watermarks
                .keyBy(new KeySelector<SensorReading, String>() {
                    @Override
                    public String getKey(SensorReading sensorReading) throws Exception {
                        return sensorReading.getId();
                    }
                })
                .window(TumblingEventTimeWindows.of(Time.seconds(10)))
                .apply(new WindowFunction<SensorReading, Tuple2<String, Double>, String, TimeWindow>() {
                    @Override
                    public void apply(String s, TimeWindow timeWindow, Iterable<SensorReading> iterable, Collector<Tuple2<String, Double>> collector) throws Exception {
                        ArrayList<SensorReading> list = Lists.newArrayList(iterable);
                        Double tempSum = list.stream().map(x -> x.getTemp()).reduce((x, y) -> x + y).get();
                        collector.collect(new Tuple2<>(s, tempSum));
                    }
                });
//        applyDS.print("applyDS:");

        //2、全窗口函数 process(ProcesswindowFunction): 计算窗口的每个传感器平均温度
        SingleOutputStreamOperator<Tuple2<String, Double>> processDS = watermarks
                .keyBy(new KeySelector<SensorReading, String>() {
                    @Override
                    public String getKey(SensorReading sensorReading) throws Exception {
                        return sensorReading.getId();
                    }
                })
                .window(TumblingEventTimeWindows.of(Time.seconds(10)))
                .process(new ProcessWindowFunction<SensorReading, Tuple2<String, Double>, String, TimeWindow>() {
                    @Override
                    public void process(String s, Context context, Iterable<SensorReading> iterable, Collector<Tuple2<String, Double>> collector) throws Exception {
                        ArrayList<SensorReading> list = Lists.newArrayList(iterable);
                        Double tempSum = list.stream().map(x -> x.getTemp()).reduce((x, y) -> x + y).get();
                        int tempCnt = list.size();
                        collector.collect(new Tuple2<>(list.get(1).getId(), tempSum / tempCnt));
                    }
                });
//        processDS.print("processDS:");

        //external_2 aggregartion + window: 计算窗口的每个传感器累计温度
        SingleOutputStreamOperator<String> processWinDS = watermarks
                .keyBy(new KeySelector<SensorReading, String>() {
                    @Override
                    public String getKey(SensorReading sensorReading) throws Exception {
                        return sensorReading.getId();
                    }
                })
                .window(TumblingEventTimeWindows.of(Time.seconds(10)))
                .aggregate(new AggregateFunction<SensorReading, Tuple2<String, Double>, Tuple2<String, Double>>() {
                               @Override
                               public Tuple2<String, Double> createAccumulator() {
                                   return new Tuple2<>(null, 0d);
                               }

                               @Override
                               public Tuple2<String, Double> add(SensorReading sensorReading, Tuple2<String, Double> t2) {
                                   return new Tuple2<>(sensorReading.getId(), t2.f1 + sensorReading.getTimestamp());
                               }

                               @Override
                               public Tuple2<String, Double> getResult(Tuple2<String, Double> t2) {
                                   return new Tuple2<>(t2.f0, t2.f1);
                               }

                               @Override
                               public Tuple2<String, Double> merge(Tuple2<String, Double> stringDoubleTuple2, Tuple2<String, Double> acc1) {
                                   return null;
                               }
                           },
                        new ProcessWindowFunction<Tuple2<String, Double>, String, String, TimeWindow>() {
                            @Override
                            public void process(String s, Context context, Iterable<Tuple2<String, Double>> iterable, Collector<String> collector) throws Exception {
                                Tuple2<String, Double> t2 = iterable.iterator().next();
                                collector.collect(context.window().getStart() + ": " + t2);
                            }
                        }
                );
        processWinDS.print("processWinDS:");


        //3、计数窗口
        SingleOutputStreamOperator<Double> countWin = watermarks
                .keyBy("id")
                .countWindow(3, 1)
                .aggregate(new AggregateFunction<SensorReading, Tuple2<Double, Integer>, Double>() {
                    @Override
                    public Tuple2<Double, Integer> createAccumulator() {
                        return new Tuple2<>(0.0, 0);
                    }

                    @Override
                    public Tuple2<Double, Integer> add(SensorReading sensorReading, Tuple2<Double, Integer> t2) {
                        return new Tuple2<>(sensorReading.getTemp() + t2.f0, t2.f1 + 1);
                    }

                    @Override
                    public Double getResult(Tuple2<Double, Integer> t2) {
                        return t2.f0 / t2.f1;
                    }

                    @Override
                    public Tuple2<Double, Integer> merge(Tuple2<Double, Integer> t2, Tuple2<Double, Integer> acc1) {
                        return new Tuple2<>(t2.f0 + acc1.f0, t2.f1 + acc1.f1);
                    }
                });
//        countWin.print();

        //4、其他可选api
//        watermarks.keyBy("id")
//                .timeWindow(Time.seconds(4))
//                .trigger() 触发器
//        .evictor() 移除器
//        .allowedLateness(Time.seconds(2)) 允许迟到数据
//        .sideOutputLateData(new OutputTag<>("aa"))  迟到数据侧输出流

//        countWin.getSideOutput(new OutputTag<>("test"))  获取数据侧输出流


        env.execute();

    }


}
