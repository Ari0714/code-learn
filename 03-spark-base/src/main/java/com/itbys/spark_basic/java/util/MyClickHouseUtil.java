package com.itbys.spark_basic.java.util;

import com.itbys.spark_basic.java.common.CommonConfig;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;

import java.util.Properties;

/**
 * Author Ari
 * Date 2022/4/11
 * Desc
 */
public class MyClickHouseUtil {

    static Properties properties = new Properties();

    static {
        properties.setProperty("user", CommonConfig.clickhouse_user);
        properties.setProperty("password", CommonConfig.clickhouse_passwd);
        properties.setProperty("driver", CommonConfig.clickhouse_driver);
    }


    /**
     * @desc 往ck写入数据
     * @param: dataset
     * @param: table
     */
    public static void writeToCk(Dataset<Row> dataset, String table) {

        dataset.write()
                .mode(SaveMode.Append)
                .option("batchsize", "1000")
                .option("isolationLevel", "NONE")
                .option("numPartitions", "1")
                .jdbc(CommonConfig.clickhouse_url, table, properties);
    }

}
