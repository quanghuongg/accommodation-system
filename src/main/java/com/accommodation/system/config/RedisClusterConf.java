package com.accommodation.system.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

public class RedisClusterConf {

    private static JedisPoolConfig poolConfig() {
        final JedisPoolConfig poolConfig = new JedisPoolConfig();

        // jedis num connection must equal or larger than num thread worker
        poolConfig.setMaxTotal(8);
        poolConfig.setMaxIdle(8);
        poolConfig.setMinIdle(2);

        // max time to wait when call JedisPool.getResource() is 2 second
        poolConfig.setMaxWaitMillis(Duration.ofSeconds(2).toMillis());
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnReturn(true);
        poolConfig.setTestWhileIdle(true);
        poolConfig.setMinEvictableIdleTimeMillis(Duration.ofSeconds(60).toMillis());
        poolConfig.setTimeBetweenEvictionRunsMillis(Duration.ofSeconds(30).toMillis());
        poolConfig.setNumTestsPerEvictionRun(3);
        poolConfig.setBlockWhenExhausted(true);
        return poolConfig;
    }


    @Bean
    public JedisCluster initRedisCluster() {
        Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
        String[] listRedis = "Constant.REDIS_CLUSTER_DOMAINS".split(",");
        for (String redis : listRedis) {
            String[] hp = redis.split(":");
            HostAndPort hostAndPort = new HostAndPort(hp[0], Integer.parseInt(hp[1]));
            jedisClusterNodes.add(hostAndPort);
        }

        return new JedisCluster(jedisClusterNodes, poolConfig());
    }
}