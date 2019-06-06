package com.ajw.quondam.redis;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import redis.embedded.RedisCluster;
import redis.embedded.util.JedisUtil;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class RedisOpsTest {

    private RedisCluster cluster;
    protected Set<String> redisSentinelHosts;

    private RedissonClient client;

    @Before
    public void setup() {

        cluster = RedisCluster.builder().ephemeral().sentinelCount(3).quorumSize(2)
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

        client = Redisson.create(config);
    }

    @Test
    public void testPutKeys() {
        RMap<String, Integer> map = client.getMap("idempotence");
        map.put("1", 1);
        map.put("2", 1);
        map.put("3", 1);

        RMap<String, Integer> mapsToAssert = client.getMap("idempotence");
        assertThat(mapsToAssert.containsKey("1"), equalTo(true));
        assertThat(mapsToAssert.containsKey("2"), equalTo(true));
        assertThat(mapsToAssert.containsKey("3"), equalTo(true));
    }

    @Test
    public void testDeleteKeys() {
        RMap<String, Integer> map = client.getMap("idempotence");
        map.put("1", 1);
        map.put("2", 1);
        map.put("3", 1);

        RMap<String, Integer> mapToDeleteFrom = client.getMap("idempotence");
        mapToDeleteFrom.remove("1");
        mapToDeleteFrom.remove("2");

        RMap<String, Integer> setsToAssert = client.getMap("idempotence");
        assertThat(setsToAssert.containsKey("1"), equalTo(false));
        assertThat(setsToAssert.containsKey("2"), equalTo(false));
        assertThat(setsToAssert.containsKey("3"), equalTo(true));
    }

    @Test
    public void testLockCannotBeAcquiredByOtherThreadWhenLocked() throws InterruptedException {

        RLock lock = client.getLock("lock");
        lock.lock(2, TimeUnit.SECONDS);

        Thread t = new Thread() {
            public void run() {
                RLock sameLock = client.getLock("lock");
                try {
                    boolean locked = sameLock.tryLock(1, 1, TimeUnit.MILLISECONDS);
                    assertThat(locked, equalTo(false));
                } catch (InterruptedException ignored) {

                }
            }
        };

        t.start();
        t.join();

    }

    @Test
    public void testDifferentLockIsAcquiredByOtherThread() throws InterruptedException {

        RLock lock = client.getLock("lock");
        lock.lock(2, TimeUnit.SECONDS);

        Thread t = new Thread() {
            public void run() {
                RLock differentLock = client.getLock("differentLock");
                try {
                    boolean locked = differentLock.tryLock(1, 1, TimeUnit.MILLISECONDS);
                    assertThat(locked, equalTo(true));
                } catch (InterruptedException ignored) {

                }
            }
        };

        t.start();
        t.join();

    }

    @After
    public void tearDown() {
        cluster.stop();
    }

}