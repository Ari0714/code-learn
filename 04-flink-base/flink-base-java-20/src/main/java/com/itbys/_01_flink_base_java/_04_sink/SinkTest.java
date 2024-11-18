package com.itbys._01_flink_base_java._04_sink;

import com.itbys._01_flink_base_java.bean.SensorReading;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.RuntimeContext;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.apache.flink.streaming.connectors.elasticsearch.ElasticsearchSinkFunction;
import org.apache.flink.streaming.connectors.elasticsearch.RequestIndexer;
import org.apache.flink.streaming.connectors.elasticsearch6.ElasticsearchSink;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.apache.flink.streaming.connectors.redis.RedisSink;
import org.apache.flink.streaming.connectors.redis.common.config.FlinkJedisPoolConfig;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisCommand;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisCommandDescription;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisMapper;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.http.HttpHost;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.Requests;

import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;

//import org.apache.hadoop.hbase.client.Connection;

/**
 * Author xx
 * Date 2023/2/18
 * Desc
 */
public class SinkTest {

    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        DataStreamSource<String> fileDS = env.readTextFile("04-flink-base/flink-base-java-13/input/sensor.txt");

        SingleOutputStreamOperator<SensorReading> mapDS = fileDS.map(new MapFunction<String, SensorReading>() {
            @Override
            public SensorReading map(String s) throws Exception {
                String[] strings = s.split(",");
                return new SensorReading(strings[0], Long.parseLong(strings[1]), Double.parseDouble(strings[2]));
            }
        });

        //1、kafka
        mapDS.map(x -> x + "_test").addSink(new FlinkKafkaProducer<>("", "", new SimpleStringSchema()));

        //2、redis
        FlinkJedisPoolConfig config = new FlinkJedisPoolConfig.Builder()
                .setHost("hdp")
                .setPort(6379)
                .build();
        mapDS.addSink(new RedisSink<>(config, new MyRedisMapper()));

        //3、ES
        ArrayList<HttpHost> httpHosts = new ArrayList<>();
        httpHosts.add(new HttpHost("hdp", 9200));
        mapDS.addSink(new ElasticsearchSink.Builder<SensorReading>(httpHosts, new MyEsSinkFunction()).build());

        //4、使用jdbc
//        mapDS.addSink(JdbcSink.sink(
//                "INSERT INTO clicks (id, timestamp, temp) VALUES (?, ?, ?)",
//                new JdbcStatementBuilder<SensorReading>() {
//                    @Override
//                    public void accept(PreparedStatement preparedStatement, SensorReading sensorReading) throws SQLException {
//                        preparedStatement.setString(1, sensorReading.getId());
//                        preparedStatement.setLong(2, sensorReading.getTimestamp());
//                        preparedStatement.setDouble(3, sensorReading.getTemp());
//                    }
//                },
//                JdbcExecutionOptions.builder()
//                        .withBatchSize(1000)
//                        .withBatchIntervalMs(200)
//                        .withMaxRetries(5)
//                        .build(),
//                new JdbcConnectionOptions.JdbcConnectionOptionsBuilder()
//                        .withUrl("jdbc:mysql://hdp:3306/userbehavior")
//                        // 对于 MySQL 5.7，用"com.mysql.jdbc.Driver"
//                        .withDriverName("com.mysql.cj.jdbc.Driver")
//                        .withUsername("username")
//                        .withPassword("password")
//                        .build()
//        ));

        //5、自定义mysql sink
//        mapDS.addSink(new MyJdbcSink());

        //6、自定义hbase sink
        mapDS.addSink(new RichSinkFunction<SensorReading>() {

            org.apache.hadoop.conf.Configuration configuration; // 管理 Hbase 的配置信息,这里因为 Configuration 的重名问题，将类以完整路径导入
            org.apache.hadoop.hbase.client.Connection connection; // 管理 Hbase 连接118

            @Override
            public void open(Configuration parameters) throws Exception {
                super.open(parameters);
                configuration = HBaseConfiguration.create();
                configuration.set("hbase.zookeeper.quorum", "hadoop102:2181");
                connection = ConnectionFactory.createConnection(configuration);
            }

            @Override
            public void invoke(SensorReading value, Context context) throws Exception {

                String s = value.toString();

                Table table = connection.getTable(TableName.valueOf("test")); // 表名为 test
                Put put = new Put("rowkey".getBytes(StandardCharsets.UTF_8)); // 指定 rowkey

                put.addColumn("info".getBytes(StandardCharsets.UTF_8) // 指定列名
                        , s.getBytes(StandardCharsets.UTF_8) // 写入的数据
                        , "1".getBytes(StandardCharsets.UTF_8)); // 写入的数据
                table.put(put); // 执行 put 操作
                table.close(); // 将表关闭
            }

            @Override
            public void close() throws Exception {
                super.close();
                connection.close(); // 关闭连接
            }

        });

        env.execute();

    }


    public static class MyRedisMapper implements RedisMapper<SensorReading> {
        // 保存到redis的命令，存成哈希表
        public RedisCommandDescription getCommandDescription() {
            return new RedisCommandDescription(RedisCommand.HSET, "sensor_temp");
        }

        public String getKeyFromData(SensorReading data) {
            return data.getId();
        }

        public String getValueFromData(SensorReading data) {
            return data.getTemp().toString();
        }
    }


    public static class MyEsSinkFunction implements ElasticsearchSinkFunction<SensorReading> {
        @Override
        public void process(SensorReading element, RuntimeContext ctx, RequestIndexer indexer) {
            HashMap<String, String> dataSource = new HashMap<>();
            dataSource.put("id", element.getId());
            dataSource.put("ts", element.getTimestamp().toString());
            dataSource.put("temp", element.getTemp().toString());
            IndexRequest indexRequest = Requests.indexRequest()
                    .index("sensor")
                    .type("readingData")
                    .source(dataSource);
            indexer.add(indexRequest);
        }
    }


    public static class MyJdbcSink extends RichSinkFunction<SensorReading> {
        Connection conn = null;
        PreparedStatement insertStmt = null;
        PreparedStatement updateStmt = null;

        // open 主要是创建连接
        @Override
        public void open(Configuration parameters) throws Exception {
            conn = DriverManager.getConnection("jdbc:mysql://hdp:3306/test", "root", "123456");
            // 创建预编译器，有占位符，可传入参数
            insertStmt = conn.prepareStatement("INSERT INTO sensor_temp (id, temp) VALUES (?, ?)");
            updateStmt = conn.prepareStatement("UPDATE sensor_temp SET temp = ? WHERE id = ?");
        }

        // 调用连接，执行 sql
        @Override
        public void invoke(SensorReading value, Context context) throws Exception {
            // 执行更新语句，注意不要留 super
            updateStmt.setDouble(1, value.getTemp());
            updateStmt.setString(2, value.getId());
            updateStmt.execute();
            // 如果刚才 update 语句没有更新，那么插入
            if (updateStmt.getUpdateCount() == 0) {
                insertStmt.setString(1, value.getId());
                insertStmt.setDouble(2, value.getTemp());
                insertStmt.execute();
            }
        }

        @Override
        public void close() throws Exception {
            insertStmt.close();
            updateStmt.close();
            conn.close();
        }
    }


}
