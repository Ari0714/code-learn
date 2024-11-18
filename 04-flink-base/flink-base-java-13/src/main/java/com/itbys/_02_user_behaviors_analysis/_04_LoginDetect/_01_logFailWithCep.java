package com.itbys._02_user_behaviors_analysis._04_LoginDetect;

import com.itbys._02_user_behaviors_analysis._04_LoginDetect.bean.LoginEvent;
import com.itbys._02_user_behaviors_analysis._04_LoginDetect.bean.LoginFailWarning;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.cep.CEP;
import org.apache.flink.cep.PatternSelectFunction;
import org.apache.flink.cep.PatternStream;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.cep.pattern.conditions.SimpleCondition;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.windowing.time.Time;

import java.util.List;
import java.util.Map;

/**
 * Author xx
 * Date 2021/9/2
 * Desc cep主要实现风控类指标：cep实现2s之内连续两次登录失败
 */
public class _01_logFailWithCep {

    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        //设置时间类型
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

        //input：7328,193.114.45.13,success,1558430848
        SingleOutputStreamOperator<LoginEvent> watermarksDS = env.readTextFile("input/LoginLog.csv")
                .map(new MapFunction<String, LoginEvent>() {
                    @Override
                    public LoginEvent map(String s) throws Exception {
                        String[] strings = s.split(",");
                        return new LoginEvent(
                                Long.parseLong(strings[0]), strings[1], strings[2], Long.parseLong(strings[3]) * 1000L);
                    }
                })
                .assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor<LoginEvent>(Time.seconds(2)) {
                    @Override
                    public long extractTimestamp(LoginEvent loginEvent) {
                        return loginEvent.getTimestamp();
                    }
                });


        //1、定义匹配模式
        Pattern<LoginEvent, LoginEvent> pattern = Pattern.<LoginEvent>begin("firstFail")
                .where(new SimpleCondition<LoginEvent>() {
                    @Override
                    public boolean filter(LoginEvent loginEvent) throws Exception {
                        return "fail".equals(loginEvent.getLoginState());
                    }
                })
                .next("secondFail").where(new SimpleCondition<LoginEvent>() {
                    @Override
                    public boolean filter(LoginEvent loginEvent) throws Exception {
                        return "fail".equals(loginEvent.getLoginState());
                    }
                })
                .within(Time.seconds(2));

        //2、将模式引用在流上 拿到模式流
        PatternStream<LoginEvent> patternStream = CEP.pattern(watermarksDS.keyBy("userId"), pattern);

        //3、检出符合条件的复杂时间 进行转换处理 得到报警信息
        SingleOutputStreamOperator<LoginFailWarning> selectDS = patternStream.select(new PatterSelFunc());
//        selectDS.print();


        env.execute();

    }

    private static class PatterSelFunc implements PatternSelectFunction<LoginEvent, LoginFailWarning> {
        @Override
        public LoginFailWarning select(Map<String, List<LoginEvent>> map) throws Exception {
            LoginEvent firstFail = map.get("firstFail").iterator().next();
            LoginEvent secondFail = map.get("secondFail").get(0);
            return new LoginFailWarning(firstFail.getUserId(), firstFail.getTimestamp(), secondFail.getTimestamp(), "连续两次登录失败！");
        }
    }
}
