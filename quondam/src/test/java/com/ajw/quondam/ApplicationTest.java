package com.ajw.quondam;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.junit4.SpringRunner;
import redis.embedded.RedisCluster;
import redis.embedded.util.JedisUtil;

import java.util.Set;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTest {

    @Test
    public void contextLoads() throws Exception {
    }

    @Bean
    @Primary
    public RedissonClient redissonClient() {

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