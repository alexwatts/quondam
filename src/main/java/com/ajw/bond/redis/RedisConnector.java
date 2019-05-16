package com.ajw.bond.redis;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.util.concurrent.TimeUnit;

public class RedisConnector {

    private final RedissonClient redissonClient;

    public RedisConnector(String sentinelAddress) {

        Config config = new Config();
        config.useSentinelServers().setMasterName("master")
                .addSentinelAddress(sentinelAddress);

        redissonClient = Redisson.create(config);
    }

    public RMap<String, Integer> putKey(String shard, String key) {

        RLock lock = redissonClient.getLock(key);
        try {
            lock.lock(10, TimeUnit.SECONDS);

            // Wait for 100 seconds and automatically unlock it after 10 seconds
            boolean res = lock.tryLock(100, 10, TimeUnit.SECONDS);
            if (res) {
                try {
                    RMap<String, Integer> map = redissonClient.getMap(shard);
                    map.put(key, 1);
                    return map;
                } finally {
                    lock.unlock();
                }
            }
        } catch (InterruptedException e) {
            lock.unlock();
        }
        throw new IllegalStateException(String.format("Lock was not acquired for shard:%s, key:%s", shard, key));
    }

    public boolean removeKey(String shard, String key) {
        RLock lock = redissonClient.getLock(key);

        try {
            lock.lock(10, TimeUnit.SECONDS);
            // Wait for 100 seconds and automatically unlock it after 10 seconds
            boolean res = lock.tryLock(100, 10, TimeUnit.SECONDS);
            if (res) {
                try {
                    RMap<String, Integer> map = redissonClient.getMap(shard);
                    if (map.containsKey(key)) {
                        map.remove(key);
                        return true;
                    } else {
                        return false;
                    }
                } finally {
                    lock.unlock();
                }
            }
        } catch (InterruptedException ignored) {
            lock.unlock();
            return false;
        }
       return false;
    }

    public Boolean isConnected() {
        return !redissonClient.isShutdown();
    }

}
