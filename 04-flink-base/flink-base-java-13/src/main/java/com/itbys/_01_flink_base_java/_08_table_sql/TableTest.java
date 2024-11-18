package com.itbys._01_flink_base_java._08_table_sql;

import com.itbys._01_flink_base_java.bean.SensorReading;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.DataTypes;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.table.descriptors.Csv;
import org.apache.flink.table.descriptors.FileSystem;
import org.apache.flink.table.descriptors.Kafka;
import org.apache.flink.table.descriptors.Schema;
import org.apache.flink.types.Row;

import java.time.Duration;

/**
 * Author xx
 * Date 2023/2/19
 * Desc
 */
public class TableTest {

    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        //设置时间类型
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

//        env.setStateBackend(new MemoryStateBackend());
//        env.setStateBackend(new FsStateBackend("file://bk/"));
//        env.setStateBackend(new RocksDBStateBackend(""));

        DataStreamSource<String> fileDS = env.readTextFile("04-flink-base/flink-base-java-13/input/sensor.txt");

        SingleOutputStreamOperator<SensorReading> mapDS = fileDS.map(new MapFunction<String, SensorReading>() {
            @Override
            public SensorReading map(String s) throws Exception {
                String[] strings = s.split(",");
                return new SensorReading(strings[0], Long.parseLong(strings[1]), Double.parseDouble(strings[2]));
            }
        });

        //设置watermark
        SingleOutputStreamOperator<SensorReading> watermarks = mapDS.assignTimestampsAndWatermarks(
                WatermarkStrategy.<SensorReading>forBoundedOutOfOrderness(Duration.ofSeconds(2))
                        .withTimestampAssigner(new SerializableTimestampAssigner<SensorReading>() {
                            @Override
                            public long extractTimestamp(SensorReading sensorReading, long l) {
                                return sensorReading.getTimestamp() * 1000L;
                            }
                        })
        );


        /**
         * 简单使用
         */
        //1、创建表环境，临时表、查询、输出都得基于表环境
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);
        Table table = tableEnv.fromDataStream(watermarks);

        tableEnv.createTemporaryView("t_temp", table);

        Table sqlQuery = tableEnv.sqlQuery("select * from t_temp where temp > 30");

        tableEnv.toAppendStream(sqlQuery, Row.class).print();


        /**
         * source
         * 1、dataStream
         * 2、file
         * 3、
         */
        //2、file
        tableEnv.connect(new FileSystem().path("input/sensor.txt"))
                .withFormat(new Csv())
                .withSchema(
                        new Schema()
                                .field("id", DataTypes.STRING())
                                .field("timestamp", DataTypes.BIGINT())
                                .field("temperature", DataTypes.DOUBLE())
                ).createTemporaryTable("sensor02");

        //03.kafka es hbase hive 类似
        tableEnv.connect(new Kafka()
                .version("0.11") // 定义 kafka 的版本 .topic("sensor") // 定义主题 .property("zookeeper.connect", "localhost:2181")
                .property("bootstrap.servers", "hdp101:9092"))
                .withFormat(new Csv())
                .withSchema(new Schema()
                        .field("id", DataTypes.STRING())
                        .field("timestamp", DataTypes.BIGINT())
                        .field("temperature", DataTypes.DOUBLE()))
                .createTemporaryTable("kafkaInputTable");


        //=====sink=====
        //file  connect是个壳子  sql（“xxx”）或 inserte into
//        val fileSink: Table = tableEnv.sqlQuery("select id,temperature from sensor")

 /*   val fileOutputPath = "D:\\study\\IDEA\\01first_look_learn\\flink_first_look02\\src\\main\\resources\\sensor02.txt"
    tableEnv.connect(new FileSystem().path(fileOutputPath))
      .withFormat(new Csv)
      .withSchema(
        new Schema()
          .field("id", DataTypes.STRING())
          .field("temperature", DataTypes.DOUBLE())
      ).createTemporaryTable("sensor03")

    fileSink.insertInto("sensor03")
*/

        // 输出到 kafka
/*    tableEnv.connect( new Kafka()
      .version("0.11")
      .topic("sinkTest")
      .property("zookeeper.connect", "hdp101:2181")
      .property("bootstrap.servers", "hdp101:9092") )
      .withFormat( new Csv() )
      .withSchema( new Schema()
        .field("id", DataTypes.STRING())
        .field("temp", DataTypes.DOUBLE()) )
      .createTemporaryTable("kafkaOutputTable")

    fileSink.insertInto("kafkaOutputTable")*/


        // 输出到 es
   /* tableEnv.connect( new Elasticsearch()
      .version("6")
      .host("localhost", 9200, "http")
      .index("sensor")
      .documentType("temp") )
      .inUpsertMode() // 指定是 Upsert 模式
      .withFormat(new Json())
      .withSchema( new Schema()
        .field("id", DataTypes.STRING())
        .field("count", DataTypes.BIGINT()) )
      .createTemporaryTable("esOutputTable")

    fileSink.insertInto("esOutputTable")*/


        // 输出到 Mysql
 /*       val sinkDDL: String =
                """|create table jdbcOutputTable (
                | id varchar(20) not null,
         | cnt double not null
                |) with (
                | 'connector.type' = 'jdbc',
         | 'connector.url' = 'jdbc:mysql://hdp103:3306/room_0705',
         | 'connector.table' = 'sensor_count',
         | 'connector.driver' = 'com.mysql.jdbc.Driver',
         | 'connector.username' = 'root',
         | 'connector.password' = '111111'
                |) """
                .stripMargin*/


        env.execute();
    }


    /**
     * @desc source使用
     */
    public static void testSource() {


    }

    /**
     * @desc sink使用
     */
    public static void testSink() {


    }

}
