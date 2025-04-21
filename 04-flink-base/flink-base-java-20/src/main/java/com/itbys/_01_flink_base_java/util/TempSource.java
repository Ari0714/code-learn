package com.itbys._01_flink_base_java.util;

import com.itbys._01_flink_base_java.bean.SensorReading;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

import java.util.Random;

/**
 * Author Ari
 * Date 2023/2/19
 * Desc
 */
public class TempSource implements SourceFunction<SensorReading> {

    boolean flag = true;

    @Override
    public void run(SourceContext<SensorReading> sourceContext) throws Exception {

        Random random = new Random();

        while (flag) {
            int id = 1 + random.nextInt(10);
            long ts = System.currentTimeMillis();
            double temp = 1 + random.nextInt(40);
            sourceContext.collect(new SensorReading(id + "", ts, temp));
            Thread.sleep(10);
        }
    }

    @Override
    public void cancel() {
        flag = false;
    }
}
