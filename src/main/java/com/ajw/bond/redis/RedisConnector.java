package com.ajw.bond.redis;

import com.ajw.bond.model.IdempotenceKey;
import org.redisson.api.RLock;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisConnector {

    private final RedissonClient redissonClient;

    public RedisConnector(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    public IdempotenceKey putKey(String shard, String key) {
        RLock lock = redissonClient.getLock(key);
        try {
            lock.lock(10, TimeUnit.SECONDS);

            // Wait for 100 seconds and automatically unlock it after 10 seconds
            boolean res = lock.tryLock(100, 10, TimeUnit.SECONDS);
            if (res) {
                try {
                    RMap<String, Integer> map = redissonClient.getMap(shard);
                    map.put(key, 1);
                    return new IdempotenceKey(shard, key);
                } finally {
                    lock.unlock();
                }
            }
        } catch (InterruptedException e) {
            lock.unlock();
        } finally {
            if (lock != null && lock.isLocked()) {
                lock.unlock();
            }
        }

        throw new IllegalStateException(String.format("Lock was not acquired for shard:%s, key:%s", shard, key));
    }

    public IdempotenceKey removeKey(String shard, String key) {
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
                        return new IdempotenceKey(shard, key);
                    } else {
                        return new IdempotenceKey(shard, key);
                    }
                } finally {
                    lock.unlock();
                }
            }
        } catch (InterruptedException ignored) {
            lock.unlock();
            return new IdempotenceKey(shard, key);
        } finally {
            if (lock != null && lock.isLocked()) {
                lock.unlock();
            }
        }
        return new IdempotenceKey(shard, key);
    }

    public Boolean isConnected() {
        return !redissonClient.isShutdown();
    }

}
