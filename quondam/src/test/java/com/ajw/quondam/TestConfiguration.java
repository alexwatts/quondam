package com.ajw.quondam;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import redis.embedded.RedisCluster;
import redis.embedded.util.JedisUtil;

import java.util.Set;
import java.util.stream.Collectors;

@Configuration
public class TestConfiguration {

    @Bean
    @Primary
    public RedissonClient redissonTestClient() {

        Set<String> redisSentinelHosts;

        RedisCluster cluster = RedisCluster.builder().ephemeral().sentinelCount(3).quorumSize(2)
                .replicationGroup("master", 1)
                .build();
        cluster.start();

        redisSentinelHosts = JedisUtil.sentinelHosts(cluster);

        Config config = new Config();
        config.useSentinelServers().setMasterName("master")
                .addSentinelAddress(
                        redisSentinelHosts.stream().
                                map(s -> "redis://" + s).collect(Collectors.toList()).
                                toArray(new String[redisSentinelHosts.size()])
                );

        return Redisson.create(config);

    }
}
