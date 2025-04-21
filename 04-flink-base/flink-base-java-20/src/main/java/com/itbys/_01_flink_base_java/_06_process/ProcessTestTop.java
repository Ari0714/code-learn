package com.itbys._01_flink_base_java._06_process;

import com.google.common.collect.Lists;
import com.itbys._01_flink_base_java.bean.SensorReading;
import com.itbys._01_flink_base_java.util.TempSource;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.ProcessAllWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


/**
 * Author Ari
 * Date 2023/2/19
 * Desc 应用：需要统计最近10秒钟内最高的的两个temp sersor，并且每5秒钟更新一次
 */
public class ProcessTestTop {

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

        SingleOutputStreamOperator<String> processDS = watermarkDS
                .windowAll(TumblingEventTimeWindows.of(Time.seconds(10), Time.seconds(2)))
                .process(new ProcessAllWindowFunction<SensorReading, String, TimeWindow>() {
                    @Override  // 全聚合函数返回的是迭代器类型，处理的是所有数据
                    public void process(Context context, Iterable<SensorReading> iterable, Collector<String> collector) throws Exception {
                        ArrayList<SensorReading> list = Lists.newArrayList(iterable);

                        // 排序, 取出top3
                        list.sort(new Comparator<SensorReading>() {
                            @Override
                            public int compare(SensorReading o1, SensorReading o2) {
                                return (int) (o2.getTemp() - o1.getTemp());
                            }
                        });
                        List<SensorReading> subList = list.subList(0, 3);

                        StringBuffer stringBuffer = new StringBuffer("==========\n");
                        for (SensorReading sensorReading : subList) {
                            stringBuffer.append(sensorReading.getId() + ": " + sensorReading.getTemp()+"\n");
                        }
                        stringBuffer.append("==========\n");

                        collector.collect(stringBuffer.toString());
                    }
                });
        processDS.print("processDSTop: ");


        env.execute();

    }

}
