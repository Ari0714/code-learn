package com.itbys._02_user_behaviors_analysis._02_NetWorkFlowAnalysis;

import com.itbys._02_user_behaviors_analysis.bean.UserBehaviors;
import com.itbys._02_user_behaviors_analysis.bean.UvCount;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.functions.windowing.AllWindowFunction;
import org.apache.flink.streaming.api.functions.windowing.ProcessAllWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.triggers.Trigger;
import org.apache.flink.streaming.api.windowing.triggers.TriggerResult;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
import redis.clients.jedis.Jedis;

import java.time.Duration;
import java.util.HashSet;


/**
 * Author xx
 * Date 2023/2/19
 * Desc 实时统计每小时内的网站uv， 注意去重
 */
public class _03_uv {

    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        //设置时间类型
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

        DataStreamSource<String> fileDS = env.readTextFile("04-flink-base/flink-base-java-13/input/UserBehavior.csv");

        SingleOutputStreamOperator<UserBehaviors> mapDS = fileDS.map(new MapFunction<String, UserBehaviors>() {
            @Override
            public UserBehaviors map(String s) throws Exception {
                String[] strings = s.split(",");
                return new UserBehaviors(Long.parseLong(strings[0]), Long.parseLong(strings[1]), Integer.parseInt(strings[2]), strings[3], Long.parseLong(strings[4]));
            }
        });

        //设置watermark
        SingleOutputStreamOperator<UserBehaviors> watermarks = mapDS.assignTimestampsAndWatermarks(
                WatermarkStrategy.<UserBehaviors>forBoundedOutOfOrderness(Duration.ofSeconds(2))
                        .withTimestampAssigner(new SerializableTimestampAssigner<UserBehaviors>() {
                            @Override
                            public long extractTimestamp(UserBehaviors userBehaviors, long l) {
                                return userBehaviors.getTimestamp() * 1000L;
                            }
                        })
        );

        // 计算
        SingleOutputStreamOperator<Tuple2<Long, Integer>> uvDS = watermarks
                .filter(x -> "pv".equals(x.getBehavior()))
                .windowAll(TumblingEventTimeWindows.of(Time.hours(1)))
                .apply(new AllWindowFunction<UserBehaviors, Tuple2<Long, Integer>, TimeWindow>() {
                    @Override
                    public void apply(TimeWindow timeWindow, Iterable<UserBehaviors> iterable, Collector<Tuple2<Long, Integer>> collector) throws Exception {
                        HashSet<String> set = new HashSet<>();
                        for (UserBehaviors userBehaviors : iterable) {
                            set.add(userBehaviors.getUserId().toString());
                        }
                        collector.collect(new Tuple2<>(timeWindow.getStart(), set.size()));

                    }
                });
        uvDS.print();


        env.execute();


//        testUvWithBloom();

    }


    /**
     * @desc uv: 使用bloom过滤器去重
     */
    public static void testUvWithBloom() throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        //设置时间类型
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

        DataStreamSource<String> fileDS = env.readTextFile("input/UserBehavior.csv");

        SingleOutputStreamOperator<UserBehaviors> mapDS = fileDS.map(new MapFunction<String, UserBehaviors>() {
            @Override
            public UserBehaviors map(String s) throws Exception {
                String[] strings = s.split(",");
                return new UserBehaviors(Long.parseLong(strings[0]), Long.parseLong(strings[1]), Integer.parseInt(strings[2]), strings[3], Long.parseLong(strings[4]));
            }
        });

        //设置watermark
        SingleOutputStreamOperator<UserBehaviors> watermarks = mapDS.assignTimestampsAndWatermarks(
                new BoundedOutOfOrdernessTimestampExtractor<UserBehaviors>(Time.seconds(2)) {
                    @Override
                    public long extractTimestamp(UserBehaviors userBehaviors) {
                        return userBehaviors.getTimestamp() * 1000L;
                    }
                });

        // 计算
        SingleOutputStreamOperator<UvCount> uvProcess = watermarks
                .filter(x -> "uv".equals(x.getBehavior()))
                .windowAll(TumblingEventTimeWindows.of(Time.seconds(5)))
                .trigger(new MyTrigger())
                .process(new ProcesAllWinFunBloom());
        uvProcess.print();


        env.execute();

    }

    public static class MyTrigger extends Trigger<UserBehaviors, TimeWindow> {
        @Override
        public TriggerResult onElement(UserBehaviors userBehaviors, long l, TimeWindow timeWindow, TriggerContext triggerContext) throws Exception {
            return TriggerResult.FIRE_AND_PURGE;
        }

        @Override
        public TriggerResult onProcessingTime(long l, TimeWindow timeWindow, TriggerContext triggerContext) throws Exception {
            return TriggerResult.CONTINUE;
        }

        @Override
        public TriggerResult onEventTime(long l, TimeWindow timeWindow, TriggerContext triggerContext) throws Exception {
            return TriggerResult.CONTINUE;
        }

        @Override
        public void clear(TimeWindow timeWindow, TriggerContext triggerContext) throws Exception {

        }
    }

    //自定义bloom过滤器
    public static class MyBloomFilter {

        //自定义位图大小 一般为2的整次幂
        private Integer cap;

        public MyBloomFilter(Integer cap) {
            this.cap = cap;
        }

        //实现一个hash函数
        public Long hasCode(String value, Integer seed) {
            Long result = 0L;
            for (int i = 0; i < value.length(); i++) {
                result = result * seed + value.charAt(i);
            }
            return result & (cap - 1);
        }
    }


    private static class ProcesAllWinFunBloom extends ProcessAllWindowFunction<UserBehaviors, UvCount, TimeWindow> {

        Jedis jedis;
        MyBloomFilter bloom;
        final String uvCountMapName = "uvCount";

        @Override
        public void open(Configuration parameters) throws Exception {
            jedis = new Jedis("hdp");
            bloom = new MyBloomFilter(1 << 29);   //要处理一亿个数据  用64M
        }

        @Override
        public void process(Context context, Iterable<UserBehaviors> iterable, Collector<UvCount> collector) throws Exception {

            Long windowStart = context.window().getStart();
            Long windowEnd = context.window().getEnd();
            String bitmapKey = String.valueOf(windowEnd);
            String uvCountKey = String.valueOf(windowEnd);
            String userId = iterable.iterator().next().getUserId().toString();
            Long offset = bloom.hasCode(userId, 61);
            Boolean isExist = jedis.getbit(bitmapKey, offset);
            if (!isExist) {
                jedis.setbit(bitmapKey, offset, true);
                Long uvCount = 0L;
                String uvCountStr = jedis.hget(uvCountMapName, uvCountKey);
                if (uvCountStr != null && !"".equals(uvCountStr)) {
                    uvCount = Long.valueOf(uvCountStr);
                }
                jedis.hset(uvCountMapName, uvCountKey, String.valueOf(uvCount + 1));
                collector.collect(new UvCount(windowStart, windowEnd, uvCount + 1));
            }

        }

        @Override
        public void close() throws Exception {
            jedis.close();
        }
    }

}
