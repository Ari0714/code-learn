package com.itbys.common;

/**
 * Author Ari
 * Date 2024/7/15
 * Desc
 */
public class CommonConfig {

    /**
     * ck
     */
    //pre
    public static final String CkIndustryPreIp = "jdbc:clickhouse://10.68.20.29:8123?socket_timeout=6000000";
    public static final String CkIndustryPreUser = "admin";
    public static final String CkIndustryPrePassWd = "clkadmin";
    public static final String CkGroupPreIp = "jdbc:clickhouse://10.74.134.149:8123?socket_timeout=6000000";
    public static final String CkGroupPreUser = "admin";
    public static final String CkGroupPrePassWd = "clkadmin";

    //prod
    public static final String CkIndustryProdIp = "jdbc:clickhouse://10.91.2.201:8123?socket_timeout=6000000";
    public static final String CkIndustryProdUser = "admin";
    public static final String CkIndustryProdPassWd = "clkadmin";
    public static final String CkGroupProdIp = "jdbc:clickhouse://10.74.146.161:8123?socket_timeout=6000000";
    public static final String CkGroupProdUser = "admin";
    public static final String CkGroupProdPassWd = "clkadmin";

    /**
     * kafka
     */
//    public static final String kafkaCDHProdBroker = "10.91.2.103:9092,10.91.2.104:9092,10.91.2.105:9092";
//    public static final String kafkaSelfProdBroker = "10.91.2.201:9092,10.91.2.202:9092,10.91.2.203:9092";
//    public static final String kafkaSelfpreBroker = "10.68.20.28:9092,10.68.20.29:9092,10.68.20.30:9092";
//    public static final String kafkaSelfdevBroker = "10.126.124.77:9092,10.126.124.78:9092,10.126.124.79:9092";
    public static final String kafkaTCvmBroker = "114.132.159.231:9092";


    /**
     * sr
     */
//    public final String CkIndustryProd = "";
//    public final String CkGroupProd = "";


}
