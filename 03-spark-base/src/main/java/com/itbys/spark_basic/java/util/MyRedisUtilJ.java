package com.itbys.spark_basic.java.util;

import com.itbys.spark_basic.java.common.CommonConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Author chenjie
 * Date 2023/9/20
 * Desc
 */
public class MyRedisUtilJ {

    public static JedisPool jedisPool = null;

    /**
     * @desc 获取Jedis
     * @return: redis.clients.jedis.JedisPool
     */
    public static Jedis getJedisClient() {
        if (jedisPool == null) {
            JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
            jedisPoolConfig.setMaxTotal(100); //最大连接数
            jedisPoolConfig.setMaxIdle(20); //最大空闲
            jedisPoolConfig.setMinIdle(20); //最小空闲
            jedisPoolConfig.setBlockWhenExhausted(true); //忙碌时是否等待
            jedisPoolConfig.setMaxWaitMillis(500); //忙碌时等待时长 毫秒
            jedisPoolConfig.setTestOnBorrow(true); //每次获得连接的进行测试

            jedisPool = new JedisPool(jedisPoolConfig, CommonConfig.redis_ip, CommonConfig.redis_port);
        }
        return jedisPool.getResource();
    }


}
