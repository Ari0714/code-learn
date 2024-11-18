package com.itbys.spark_basic.java.test;

import org.apache.spark.SparkConf;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructType;


/**
 * Author xx
 * Date 2022/7/31
 * Desc
 */
public class SparkAnalysisJ {

    /**
     * @desc 读取Xlsx
     */
    public static void readFromXlsx() {

        SparkConf conf = new SparkConf().setAppName(SparkAnalysisJ.class.getSimpleName()).setMaster("local[*]");
        SparkSession spark = SparkSession.builder().config(conf).getOrCreate();

        StructType schema = new StructType().add("is_click", DataTypes.StringType)
                .add("brand", DataTypes.StringType)
                .add("resource_pos", DataTypes.StringType)
                .add("uid", DataTypes.StringType)
                .add("click_hour", DataTypes.StringType)
                .add("income", DataTypes.StringType)
                .add("province", DataTypes.StringType)
                .add("city", DataTypes.StringType)
                .add("ad_id", DataTypes.StringType)
                .add("benefit", DataTypes.StringType)
                .add("sex", DataTypes.StringType)
                .add("monthh", DataTypes.StringType)
                .add("profession", DataTypes.StringType)
                .add("dayy", DataTypes.StringType);

        //读取数据
        Dataset<Row> inputDF = spark.read()
                .format("com.crealytics.spark.excel")
                .option("useHeader", "true")
                .schema(schema)
                .load("input/小红书数据集.xlsx");
        inputDF.show();
        inputDF.createOrReplaceTempView("ad_click");

        spark.sql("").repartition(1).write().option("header", "true").csv("output/04");

    }


    /**
     * @desc 读取Csv
     */
    public static void readFromCsv() {

        SparkConf conf = new SparkConf().setAppName(SparkAnalysisJ.class.getSimpleName()).setMaster("local[*]");
        SparkSession spark = SparkSession.builder().config(conf).getOrCreate();

        //读取数据
        Dataset<Row> inputDF = spark.read()
                .format("csv")
                .option("header", "false")
                .option("sep", "\t")
                .load("output/small_train_format/part-00000");
        inputDF.show();
        inputDF.createOrReplaceTempView("ad_click");

        spark.sql("'").repartition(1).write().option("header", "true").csv("output/04");

    }


}
