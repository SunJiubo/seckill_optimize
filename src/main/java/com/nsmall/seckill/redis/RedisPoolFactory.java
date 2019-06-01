package com.nsmall.seckill.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @ClassName RedisPollFactory
 * @Description TODO
 * @Author sky
 * @Date 19-5-27 下午10:43
 * @Version 1.0
 */

@Service
public class RedisPoolFactory {

    @Autowired
    RedisConfig redisConfig;


    @Bean
    public JedisPool JedisFactory(){
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxIdle(redisConfig.getPoolMaxIdle());
        poolConfig.setMaxTotal(redisConfig.getPoolMaxTotal());
        poolConfig.setMaxWaitMillis(redisConfig.getPoolMaxWait() * 1000);

        JedisPool jedisPool = new JedisPool(poolConfig,redisConfig.getHost(),redisConfig.getPort(),
                redisConfig.getTimeout()*1000,redisConfig.getPassword(),0);

        return jedisPool;
    }
}
