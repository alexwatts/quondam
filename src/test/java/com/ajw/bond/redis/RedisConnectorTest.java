package com.ajw.bond.redis;

import org.junit.Before;
import org.junit.Test;
import org.redisson.Redisson;
import org.redisson.api.RMap;
import org.redisson.config.Config;
import redis.embedded.RedisCluster;
import redis.embedded.util.JedisUtil;

import java.util.Set;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class RedisConnectorTest {

    private Set<String> redisSentinelHosts;

    @Before
    public void setup() {

        RedisCluster cluster = RedisCluster.builder().ephemeral().sentinelCount(3).quorumSize(2)
                .replicationGroup("master", 1)
                .build();
        cluster.start();

        redisSentinelHosts = JedisUtil.sentinelHosts(cluster);
    }

    @Test
    public void shouldBeAbleToConnectToRedis() {
        String REDIS_SENTINEL_URL = "redis://" + redisSentinelHosts.toArray()[0];
        Config config = new Config();
        config.useSentinelServers().setMasterName("master")
                .addSentinelAddress(REDIS_SENTINEL_URL);
        RedisConnector redisConnector = new RedisConnector(Redisson.create(config));
        assertThat(redisConnector.isConnected(), equalTo(true));
    }

    @Test
    public void shouldBeAbleToPutKey() {
        String REDIS_SENTINEL_URL = "redis://" + redisSentinelHosts.toArray()[0];
        Config config = new Config();
        config.useSentinelServers().setMasterName("master")
                .addSentinelAddress(REDIS_SENTINEL_URL);
        RedisConnector redisConnector = new RedisConnector(Redisson.create(config));
        RMap<String, Integer> map = redisConnector.putKey("shard", "key");
        assertThat(map.containsKey("1"), equalTo(false));
    }

    @Test
    public void shouldBeAbleToRemoveExistingKey() {
        String REDIS_SENTINEL_URL = "redis://" + redisSentinelHosts.toArray()[0];
        Config config = new Config();
        config.useSentinelServers().setMasterName("master")
                .addSentinelAddress(REDIS_SENTINEL_URL);
        RedisConnector redisConnector = new RedisConnector(Redisson.create(config));

        redisConnector.putKey("shard", "key");

        Boolean removed = redisConnector.removeKey("shard", "key");
        assertThat(removed, equalTo(true));
    }

    @Test
    public void shouldBeAbleToRemoveNonExistentKey() {
        String REDIS_SENTINEL_URL = "redis://" + redisSentinelHosts.toArray()[0];
        Config config = new Config();
        config.useSentinelServers().setMasterName("master")
                .addSentinelAddress(REDIS_SENTINEL_URL);
        RedisConnector redisConnector = new RedisConnector(Redisson.create(config));
        Boolean removed = redisConnector.removeKey("shard", "key");
        assertThat(removed, equalTo(false));
    }

}
