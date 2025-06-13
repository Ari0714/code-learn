package com.itbys.spark_basic.java.spark_streaming;

import com.itbys.spark_basic.java.bean.BaseLog;
import com.itbys.spark_basic.java.util.MyKafkaUtil;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

/**
 * Author Ari
 * Date 2022/4/11
 * Desc
 */
public class Test {

    public static void main(String[] args) throws InterruptedException {

        SparkConf conf = new SparkConf().setAppName(Test.class.getName()).setMaster("local[*]");
        JavaStreamingContext jsc = new JavaStreamingContext(conf, Durations.seconds(5));
        SparkSession spark = SparkSession.builder().config(conf).getOrCreate();

        String topic = "topic-event";
        String groupId = "group_id";

        JavaDStream<String> valueDS = MyKafkaUtil.getKafkaStream(jsc, topic, groupId).map(x -> x.value().toString());

        valueDS.foreachRDD(new VoidFunction<JavaRDD<String>>() {
            @Override
            public void call(JavaRDD<String> stringJavaRDD) throws Exception {
                JavaRDD<BaseLog> rowRDD = stringJavaRDD.map(x -> new BaseLog(x));
                Dataset<Row> dataFrame = spark.createDataFrame(rowRDD, BaseLog.class);
                dataFrame.createOrReplaceTempView("topic_event_log");

                Dataset<Row> resDS = spark.sql("SELECT " +
                        "        get_json_object(base_log,'$.what.action') what_action, " +
                        "        get_json_object(base_log,'$.what.content') what_content, " +
                        "        get_json_object(base_log,'$.what.description') what_description, " +
                        "        get_json_object(base_log,'$.what.expand1') what_expand1, " +
                        "        get_json_object(base_log,'$.what.expand2') what_expand2, " +
                        "        get_json_object(base_log,'$.what.expand3') what_expand3, " +
                        "        get_json_object(base_log,'$.when.start') when_start, " +
                        "        get_json_object(base_log,'$.when.end') when_end, " +
                        "        from_unixtime(cast(get_json_object(base_log,'$.when.start')/ 1000 as bigint),'yyyy-MM-dd') when_expand1, " +
                        "        from_unixtime(cast(get_json_object(base_log,'$.when.start')/ 1000 as bigint),'yyyy-MM-dd HH') when_expand2, " +
                        "        from_unixtime(cast(get_json_object(base_log,'$.when.start')/ 1000 as bigint),'yyyy-MM-dd HH:mm:ss') when_expand3, " +
                        "        get_json_object(base_log,'$.where.corpId') where_corpId, " +
                        "        get_json_object(base_log,'$.where.clientInfo') where_clientInfo, " +
                        "        get_json_object(base_log,'$.where.clientType') where_clientType, " +
                        "        get_json_object(base_log,'$.where.entrance') where_entrance, " +
                        "        get_json_object(base_log,'$.where.module') where_module, " +
                        "        GET_JSON_OBJECT(base_log,'$.where.app') where_app, " +
                        "        GET_JSON_OBJECT(base_log,'$.where.area') where_area, " +
                        "        get_json_object(base_log,'$.where.expand1') where_expand1, " +
                        "        get_json_object(base_log,'$.where.expand2') where_expand2, " +
                        "        get_json_object(base_log,'$.where.expand3') where_expand3, " +
                        "        if(locate('UserID',get_json_object(base_log,'$.what.content'))>0,split(split(get_json_object(base_log,'$.what.content'),'UserID\\\\\\>\\\\\\<\\\\\\!\\\\\\[CDATA\\\\\\[')[1],'\\\\\\]\\\\\\]\\\\\\>\\\\\\<\\\\\\/UserID')[0],get_json_object(base_log,'$.who.id')) who_id, " +
                        "        get_json_object(base_log,'$.who.externalUserId') who_externalUserId, " +
                        "        if(locate('UserID',get_json_object(base_log,'$.what.content'))>0,'02',get_json_object(base_log,'$.who.role')) who_role, " +
                        "        get_json_object(base_log,'$.who.selfExternalUserId') who_selfExternalUserId, " +
                        "        get_json_object(base_log,'$.who.selfUnionId') who_selfUnionId, " +
                        "        get_json_object(base_log,'$.who.source') who_source, " +
                        "        get_json_object(base_log,'$.who.unionId') who_unionId, " +
                        "        get_json_object(base_log,'$.who.expand1') who_expand1, " +
                        "        get_json_object(base_log,'$.who.expand2') who_expand2, " +
                        "        get_json_object(base_log,'$.who.expand3') who_expand3, " +
                        "        get_json_object(base_log,'$.whom.id') whom_id, " +
                        "        get_json_object(base_log,'$.whom.externalUserId') whom_externalUserId, " +
                        "        get_json_object(base_log,'$.whom.role') whom_role, " +
                        "        get_json_object(base_log,'$.whom.selfExternalUserId') whom_selfExternalUserId, " +
                        "        get_json_object(base_log,'$.whom.selfUnionId') whom_selfUnionId, " +
                        "        get_json_object(base_log,'$.whom.source') whom_source, " +
                        "        get_json_object(base_log,'$.whom.unionId') whom_unionId, " +
                        "        get_json_object(base_log,'$.whom.expand1') whom_expand1, " +
                        "        get_json_object(base_log,'$.whom.expand2') whom_expand2, " +
                        "        get_json_object(base_log,'$.whom.expand3') whom_expand3, " +
                        "        date_format(CURRENT_DATE,'yyyyMMdd') ds " +
                        "from ( " +
                        "        select split(logs,'#EVENT-CDH@')[1] base_log " +
                        "from " +
                        "        (select get_json_object(line,'$.message') logs " +
                        "        from topic_event_log) a " +
                        "     )b");

                resDS.show();

                //写入ck
//                MyClickHouseUtil.writeToCk(resDS,"aadd");
            }

            //提交偏移量

        });


        jsc.start();
        jsc.awaitTermination();

    }

}
