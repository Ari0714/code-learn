package com.itbys._01_flink_base_java._06_process;

import com.itbys._01_flink_base_java.bean.SensorReading;
import com.itbys._01_flink_base_java.util.TempSource;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

import java.time.Duration;


/**
 * Author Ari
 * Date 2023/2/19
 * Desc 温度值连续10S上升，报警
 */
public class ProcessTest {

    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        //设置时间类型
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

        // 状态后端
//        env.setStateBackend(new MemoryStateBackend());
//        env.setStateBackend(new FsStateBackend("file://bk/"));
//        env.setStateBackend(new RocksDBStateBackend(""));

        DataStreamSource<SensorReading> mapDS = env.addSource(new TempSource());

        //设置watermark
        SingleOutputStreamOperator<SensorReading> watermarksDS = mapDS
                .assignTimestampsAndWatermarks(
                        WatermarkStrategy.<SensorReading>forBoundedOutOfOrderness(Duration.ofSeconds(2))
                                .withTimestampAssigner(new SerializableTimestampAssigner<SensorReading>() {
                                    @Override
                                    public long extractTimestamp(SensorReading sensorReading, long l) {
                                        return sensorReading.getTimestamp();
                                    }
                                })
                );

        //1、定时器
        SingleOutputStreamOperator<String> processDS = watermarksDS
                .keyBy("id")
                .process(new KeyedProcessFunction<Tuple, SensorReading, String>() {

                    ValueState<Double> stateCnt;
                    //定时器时间
                    ValueState<Long> stateTs;

                    @Override
                    public void open(Configuration parameters) throws Exception {
                        stateCnt = getRuntimeContext().getState(new ValueStateDescriptor<Double>("cnt", Double.class, Double.MIN_VALUE));
                        stateTs = getRuntimeContext().getState(new ValueStateDescriptor<Long>("cnt2", Long.class));
                    }

                    @Override
                    public void processElement(SensorReading sensorReading, Context context, Collector<String> collector) throws Exception {

                        Double lastTmp = stateCnt.value();
                        Long lastTs = stateTs.value();

                        if (sensorReading.getTemp() > lastTmp && lastTs == null) {
                            context.timerService().registerEventTimeTimer(context.timestamp() + 1000 * 10L);
                            stateTs.update(context.timestamp() + 1000 * 10L);
                        } else if (sensorReading.getTemp() < lastTmp && lastTs != null) {
                            context.timerService().deleteEventTimeTimer(lastTs);
                            stateTs.clear();
                        }

                        stateCnt.update(sensorReading.getTemp());

                    }

                    @Override
                    public void onTimer(long timestamp, OnTimerContext ctx, Collector<String> out) throws Exception {
                        out.collect("warning");
                        stateTs.clear();
                    }

                    @Override
                    public void close() throws Exception {
                        stateCnt.clear();
                        stateTs.clear();
                    }
                });
//        processDS.print();

        //2、自定义侧输出流
        SingleOutputStreamOperator<SensorReading> outDS = watermarksDS
                .process(new ProcessFunction<SensorReading, SensorReading>() {
                    @Override
                    public void processElement(SensorReading sensorReading, Context context, Collector<SensorReading> collector) throws Exception {
                        if (sensorReading.getTemp() > 30)
                            collector.collect(sensorReading);
                        else
                            context.output(new OutputTag<>("out"), sensorReading);
                    }
                });

        DataStream<Object> sideOutput = outDS.getSideOutput(new OutputTag<>("test"));
        sideOutput.print();


        env.execute();

    }

}
