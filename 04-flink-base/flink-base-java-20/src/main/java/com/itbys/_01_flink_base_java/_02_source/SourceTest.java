package com.itbys._01_flink_base_java._02_source;

import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

import java.util.UUID;

/**
 * Author xx
 * Date 2023/2/18
 * Desc
 */
public class SourceTest {

    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        //1、从集合
//        DataStreamSource<SensorReading> collectionDS = env.fromCollection(
//                Arrays.asList(
//                        new SensorReading("sensor_1", 1547718199L, 35.8),
//                        new SensorReading("sensor_6", 1547718201L, 15.4),
//                        new SensorReading("sensor_7", 1547718202L, 6.7),
//                        new SensorReading("sensor_10", 1547718205L, 38.1)
//                )
//        );
//        collectionDS.print();


        //2、直接从element
//        DataStreamSource<Integer> elementsDS = env.fromElements(2, 34, 6, 2, 2, 5);
//        elementsDS.print();


        //3、从socket
//        DataStreamSource<String> socketDS = env.socketTextStream("localhost", 7777);
//        elementsDS.print();


        //4、file
//        DataStreamSource<String> fileDS = env.readTextFile("input/words.txt");
//        fileDS.print();


        //5、kafka
//        Properties properties = new Properties();
//        properties.setProperty("bootstrap.servers", "hdp:9092");
//        properties.setProperty("group.id", "consumer-group");
//        properties.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
//        properties.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
//        properties.setProperty("auto.offset.reset", "latest");
//
//        DataStreamSource<String> kafkaDS = env.addSource(new FlinkKafkaConsumer<String>("test", new SimpleStringSchema(), properties));


        //6、自定义
        DataStreamSource<String> selfDS = env.addSource(new SourceFunction<String>() {

            boolean flag = true;

            @Override
            public void run(SourceContext<String> sourceContext) throws Exception {

                while (flag) {
                    sourceContext.collect(UUID.randomUUID().toString());
                    Thread.sleep(100);
                }

            }

            @Override
            public void cancel() {
                flag = false;
            }
        });
        selfDS.print();


        env.execute();

    }

}
