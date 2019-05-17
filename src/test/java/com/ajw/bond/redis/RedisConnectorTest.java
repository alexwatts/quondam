package com.ajw.bond.redis;

import com.ajw.bond.model.IdempotenceKey;
import org.junit.Before;
import org.junit.Test;
import org.redisson.Redisson;
import org.redisson.config.Config;
import redis.embedded.RedisCluster;
import redis.embedded.util.JedisUtil;

import java.util.Set;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.mockito.ArgumentMatchers.isNotNull;
import static org.mockito.ArgumentMatchers.notNull;

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
        IdempotenceKey key = redisConnector.putKey("shard", "key");
        assertThat(key, is(notNullValue()));
    }

    @Test
    public void shouldBeAbleToRemoveExistingKey() {
        String REDIS_SENTINEL_URL = "redis://" + redisSentinelHosts.toArray()[0];
        Config config = new Config();
        config.useSentinelServers().setMasterName("master")
                .addSentinelAddress(REDIS_SENTINEL_URL);
        RedisConnector redisConnector = new RedisConnector(Redisson.create(config));

        redisConnector.putKey("shard", "key");

        IdempotenceKey key = redisConnector.removeKey("shard", "key");
        assertThat(key, is(notNullValue()));
    }

    @Test
    public void shouldBeAbleToRemoveNonExistentKey() {
        String REDIS_SENTINEL_URL = "redis://" + redisSentinelHosts.toArray()[0];
        Config config = new Config();
        config.useSentinelServers().setMasterName("master")
                .addSentinelAddress(REDIS_SENTINEL_URL);
        RedisConnector redisConnector = new RedisConnector(Redisson.create(config));
        IdempotenceKey key = redisConnector.removeKey("shard", "key");
        assertThat(key, is(notNullValue()));
    }

}
