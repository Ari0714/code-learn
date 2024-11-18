package com.itbys._02_user_behaviors_analysis._05_orderTimeout;

import com.itbys._02_user_behaviors_analysis._05_orderTimeout.bean.OrderEvent;
import com.itbys._02_user_behaviors_analysis._05_orderTimeout.bean.OrderResult;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.cep.CEP;
import org.apache.flink.cep.PatternSelectFunction;
import org.apache.flink.cep.PatternStream;
import org.apache.flink.cep.PatternTimeoutFunction;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.cep.pattern.conditions.SimpleCondition;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.OutputTag;

import java.util.List;
import java.util.Map;

/**
 * Author xx
 * Date 2021/9/2
 * Desc 下单到支付超过15min报警
 */
public class _01_logFailWithCep {

    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        //设置时间类型
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

        //input：34731,pay,35jue34we,1558430849
        SingleOutputStreamOperator<OrderEvent> watermarksDS = env.readTextFile("input/OrderLog.csv")
                .map(new MapFunction<String, OrderEvent>() {
                    @Override
                    public OrderEvent map(String s) throws Exception {
                        String[] strings = s.split(",");
                        return new OrderEvent(
                                Long.parseLong(strings[0]), strings[1], strings[2], Long.parseLong(strings[3]) * 1000L);
                    }
                })
                .assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor<OrderEvent>(Time.seconds(2)) {
                    @Override
                    public long extractTimestamp(OrderEvent orderEvent) {
                        return orderEvent.getTimestamp();
                    }
                });

        //1、定义匹配模式
        Pattern<OrderEvent, OrderEvent> pattern = Pattern.<OrderEvent>begin("create")
                .where(new SimpleCondition<OrderEvent>() {
                    @Override
                    public boolean filter(OrderEvent orderEvent) throws Exception {
                        return "create".equals(orderEvent.getEventType());
                    }
                })
                .followedBy("pay").where(new SimpleCondition<OrderEvent>() {
                    @Override
                    public boolean filter(OrderEvent orderEvent) throws Exception {
                        return "pay".equals(orderEvent.getEventType());
                    }
                })
                .within(Time.minutes(15));

        //2、将模式引用在流上 拿到模式流
        PatternStream<OrderEvent> patternStream = CEP.pattern(watermarksDS.keyBy("orderId"), pattern);

        //3、检出符合条件的复杂时间 进行转换处理 得到报警信息
        OutputTag<OrderResult> orderResultOutputTag = new OutputTag<OrderResult>("order_timeout") {};
        SingleOutputStreamOperator<OrderResult> selectDS = patternStream.select(orderResultOutputTag, new ParTimeoutFunc(), new ParSelFnnc());

        selectDS.getSideOutput(orderResultOutputTag).print();
        selectDS.print();

        env.execute();
    }

    private static class ParTimeoutFunc implements PatternTimeoutFunction<OrderEvent, OrderResult> {
        @Override
        public OrderResult timeout(Map<String, List<OrderEvent>> map, long l) throws Exception {
            Long orderId = map.get("create").get(0).getOrderId();
            return new OrderResult(orderId, "timeout_pay_time: " + l);
        }
    }

    private static class ParSelFnnc implements PatternSelectFunction<OrderEvent, OrderResult> {
        @Override
        public OrderResult select(Map<String, List<OrderEvent>> map) throws Exception {
            Long orderId = map.get("pay").get(0).getOrderId();
            return new OrderResult(orderId, "success_pay");
        }
    }
}
