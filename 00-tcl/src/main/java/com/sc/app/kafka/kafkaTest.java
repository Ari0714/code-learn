package com.sc.app.kafka;

import com.sc.common.CommonConfig;
import com.sc.util.MyDateUtil;
import com.sc.util.MyKafkaUtil;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.UUID;

/**
 * Author chenjie
 * Date 2024/7/22
 * Desc
 */
public class kafkaTest {

    static String kafkaBroker = CommonConfig.kafkaSelfProdBroker;

    public static void main(String[] args) throws InterruptedException {

        KafkaProducer kafkaProducer = MyKafkaUtil.testProducer(kafkaBroker);

        if (args[0].equals("pro")) {
            for (int i = 0; i <= 100000; i++) {
                String uuid = UUID.randomUUID().toString();
                kafkaProducer.send(new ProducerRecord<>(args[1], i + ": " + uuid + ": " + MyDateUtil.getYYYYMMDD_HHMMSS()));

                System.out.println(i);

                Thread.sleep(100);
            }
        } else if (args[0].equals("com")) {
            MyKafkaUtil.testComsumer(kafkaBroker,args[1]);
        }

//        testComsumer();

    }


}
