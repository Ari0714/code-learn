package com.itbys.util;

import com.itbys.common.CommonConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Arrays;
import java.util.Properties;
import java.util.UUID;

public class MyKafkaUtil {

    static String kafkaBroker = CommonConfig.kafkaTCvmBroker;

    public static void main(String[] args) throws InterruptedException {

        KafkaProducer kafkaProducer = testProducer();

//        if (args[0].equals("pro")) {
//            for (int i = 0; i < 10; i++) {
//                String uuid = UUID.randomUUID().toString();
//                kafkaProducer.send(new ProducerRecord<>("abo", i + ": " + uuid + ": " + MyDateUtil.getYYYYMMDD_HHMMSS()));
//
//                System.out.println(i);
//
//            Thread.sleep(10);
//            }
//        }
//        else if (args[0].equals("com")){
            testComsumer();
//        }

//        testComsumer();

    }


    /**
     * @desc 发送数据
     */
    public static KafkaProducer testProducer() throws InterruptedException {
        Properties kafkaProps = new Properties();
        kafkaProps.put("bootstrap.servers", kafkaBroker);
        kafkaProps.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        kafkaProps.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        kafkaProps.put("acks", "1");
//        kafkaProps.put("batch.size", "1048576");
//        kafkaProps.put("linger.ms", "5");
//        kafkaProps.put("compression.type", "snappy");
//        kafkaProps.put("buffer.memory", "33554432");

        return new KafkaProducer<String, String>(kafkaProps);

    }


    /**
     * @desc 消费数据
     */
    public static void testComsumer() {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", kafkaBroker);
        properties.put("group.id", "group-07");
        properties.put("enable.auto.commit", "true");
        properties.put("auto.commit.interval.ms", "1000");
        properties.put("auto.offset.reset", "earliest");
        properties.put("session.timeout.ms", "15000");
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(properties);
        kafkaConsumer.subscribe(Arrays.asList("abo"));

        while (true) {
            ConsumerRecords<String, String> records = kafkaConsumer.poll(100);
            for (ConsumerRecord<String, String> record : records) {
                System.out.println(record.value());
            }
        }
    }

}
