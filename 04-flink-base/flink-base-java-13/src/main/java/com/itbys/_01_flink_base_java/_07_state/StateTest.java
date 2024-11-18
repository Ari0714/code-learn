package com.itbys._01_flink_base_java._07_state;

import com.itbys._01_flink_base_java.bean.SensorReading;
import com.itbys._01_flink_base_java.util.TempSource;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.time.Duration;

/**
 * Author xx
 * Date 2023/2/19
 * Desc
 */
public class StateTest {

    public static void main(String[] args) throws Exception {

//        testSumSensor();
        testTempJump();

    }


    /**
     * @desc 连续两次温度跳变超过20度，报警
     */
    public static void testTempJump() throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        //设置时间类型
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

//        env.setStateBackend(new MemoryStateBackend());
//        env.setStateBackend(new FsStateBackend("file://bk/"));
//        env.setStateBackend(new RocksDBStateBackend(""));

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

        // 计算
        SingleOutputStreamOperator<String> stateDS = watermarkDS
                .keyBy(x -> x.getId())
                .map(new RichMapFunction<SensorReading, String>() {
                    ValueState<Double> execcedTempVs = null;

                    @Override
                    public void open(Configuration parameters) throws Exception {
                        execcedTempVs = getRuntimeContext().getState(new ValueStateDescriptor<Double>("execcedTemp", Types.DOUBLE));
                    }

                    @Override
                    public String map(SensorReading sensorReading) throws Exception {
                        Double temp = sensorReading.getTemp();
                        // 排除初次无状态
                        if (execcedTempVs.value() != null) {

                            Double tempVs = execcedTempVs.value();

                            if (Math.abs(temp - tempVs) > 20) {
                                return "温度跳变警告：" + execcedTempVs.value() + ", " + temp;
                            }
                        }

                        // 更新最新数据到状态
                        execcedTempVs.update(temp);

                        return null;
                    }
                })
                .filter(x -> x != null);
        stateDS.print();


        env.execute();

    }


    /**
     * @desc 使用状态统计每个sensor的气温累加
     */
    public static void testSumSensor() throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        //设置时间类型
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

//        env.setStateBackend(new MemoryStateBackend());
//        env.setStateBackend(new FsStateBackend("file://bk/"));
//        env.setStateBackend(new RocksDBStateBackend(""));

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

        // 使用
        SingleOutputStreamOperator<String> stateDS = watermarkDS
                .keyBy(x -> x.getId())
                .map(new RichMapFunction<SensorReading, String>() {
                    ValueState<Double> tempCntVs = null;

                    @Override
                    public void open(Configuration parameters) throws Exception {
                        tempCntVs = getRuntimeContext().getState(new ValueStateDescriptor<Double>("tempCnt", Types.DOUBLE));
                    }

                    @Override
                    public String map(SensorReading sensorReading) throws Exception {

                        Double tempState = 0d;
                        if (tempCntVs.value() != null)
                            tempState = tempCntVs.value();
                        Double temp = sensorReading.getTemp();
                        double tempSum = tempState + temp;

                        tempCntVs.update(tempState + temp);

                        return sensorReading.getId() + ": " + tempSum;
                    }
                });
        stateDS.print();


        env.execute();

    }

}
