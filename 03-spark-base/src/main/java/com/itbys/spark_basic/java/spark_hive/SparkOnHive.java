package com.itbys.spark_basic.java.spark_hive;

import com.itbys.spark_basic.java.bean.DataModel;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.util.List;

/**
 * Author xx
 * Date 2021/8/2
 * Desc spark读取hive数据
 */
public class SparkOnHive {

    public static void main(String[] args) {

        SparkConf conf = new SparkConf().setAppName("SparkOnHive").setMaster("local[*]");
        SparkSession spark = SparkSession.builder().config(conf).enableHiveSupport().getOrCreate();

        Dataset<Row> dataset = spark.sql("select * from user_info");

        JavaRDD<Row> rowJavaRDD = dataset.toJavaRDD();

        JavaRDD<DataModel> modelJavaRDD = rowJavaRDD.map(new Function<Row, DataModel>() {
            @Override
            public DataModel call(Row v1) throws Exception {

                String id = v1.getString(0);
                String item = v1.getString(1);
                String category = v1.getString(2);
                String behavior = v1.getString(3);
                String dt = v1.getString(4);
                Integer hourr = v1.getInt(5);

                return new DataModel(id, item, category, behavior, dt, hourr);
            }
        });

        List<DataModel> list = modelJavaRDD.collect();

        for (DataModel dataModel : list) {
            System.out.println(dataModel);
        }


    }
}
