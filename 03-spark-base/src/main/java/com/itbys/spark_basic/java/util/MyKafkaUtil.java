package com.itbys.spark_basic.java.util;

import com.itbys.spark_basic.java.common.CommonConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Author Ari
 * Date 2022/4/11
 * Desc
 */
public class MyKafkaUtil {

    static String kafka_broker_list = "hdp:9092";


    /**
     * =====接收消息=====
     */
    static HashMap<String, Object> hashMap = new HashMap<>();

    static {
        hashMap.put("bootstrap.servers", kafka_broker_list);
        hashMap.put("key.deserializer", StringDeserializer.class);
        hashMap.put("value.deserializer", StringDeserializer.class);
        hashMap.put("group.id", "default_group_id");
        hashMap.put("auto.offset.reset", "earliest");  //earliest
        hashMap.put("enable.auto.commit", false);
    }


    /**
     * @desc 创建DStream，返回接收到的输入数据   使用默认的消费者组
     * @param: jsc
     * @param: topic
     * @return: org.apache.spark.streaming.api.java.JavaInputDStream<org.apache.kafka.clients.consumer.ConsumerRecord               <               java.lang.Object               ,               java.lang.Object>>
     */
    public static JavaInputDStream<ConsumerRecord<Object, Object>> getKafkaStream(JavaStreamingContext jsc, String topic) {
        return KafkaUtils.createDirectStream(jsc, LocationStrategies.PreferConsistent(), ConsumerStrategies.Subscribe(Collections.singletonList(topic), hashMap));
    }


    /**
     * @desc 在对Kafka数据进行消费的时候，指定消费者组
     * @param: jsc
     * @param: topic
     * @param: groupId
     * @return: org.apache.spark.streaming.api.java.JavaInputDStream<org.apache.kafka.clients.consumer.ConsumerRecord               <               java.lang.Object               ,               java.lang.Object>>
     */
    public static JavaInputDStream<ConsumerRecord<Object, Object>> getKafkaStream(JavaStreamingContext jsc, String topic, String groupId) {
        hashMap.put("group.id", groupId);
        return KafkaUtils.createDirectStream(jsc, LocationStrategies.PreferConsistent(), ConsumerStrategies.Subscribe(Collections.singletonList(topic), hashMap));
    }


    /**
     * @desc 从指定的偏移量位置读取数据
     * @param: jsc
     * @param: topic
     * @param: groupId
     * @param: offsets
     * @return: org.apache.spark.streaming.api.java.JavaInputDStream<org.apache.kafka.clients.consumer.ConsumerRecord               <               java.lang.Object               ,               java.lang.Object>>
     */
    public static JavaInputDStream<ConsumerRecord<Object, Object>> getKafkaStream(JavaStreamingContext jsc, String topic, String groupId, Map<TopicPartition, Long> offsets) {
        hashMap.put("group.id", groupId);
        return KafkaUtils.createDirectStream(jsc, LocationStrategies.PreferConsistent(), ConsumerStrategies.Subscribe(Collections.singletonList(topic), hashMap, offsets));
    }


    /**
     * =====发送消息=====
     */
    public static void producerToKafka(String topic, String info) throws InterruptedException {
        Properties kafkaProps = new Properties();
        kafkaProps.put("bootstrap.servers", kafka_broker_list);
        kafkaProps.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        kafkaProps.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(kafkaProps);

        //发送数据
        producer.send(new ProducerRecord<>(topic, info));

    }


}
