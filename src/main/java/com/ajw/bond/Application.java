package com.ajw.bond;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
public class Application {

    @Value(value = "redis://${REDIS_SENTINEL_SERVICE_HOST:10.0.0.1}:${REDIS_SENTINEL_SERVICE_PORT:26739}")
    public String sentinelAddress;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Profile({"Integrated"})
    @Bean
    public RedissonClient redissonClient() {

        Config config = new Config();
        config.useSentinelServers().setMasterName("mymaster")
                .addSentinelAddress(sentinelAddress);

        return Redisson.create(config);
    }

}
