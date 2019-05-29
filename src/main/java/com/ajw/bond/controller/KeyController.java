package com.ajw.bond.controller;

import com.ajw.bond.model.IdempotenceKey;
import com.ajw.bond.redis.RedisConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KeyController {

    @Autowired
    public RedisConnector redisConnector;

    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/keys", method = RequestMethod.PUT)
    public IdempotenceKey putKey(@RequestBody IdempotenceKey idempotenceKey) {
        return redisConnector.putKey(idempotenceKey.getShard(), idempotenceKey.getKey());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/keys", method = RequestMethod.DELETE)
    public IdempotenceKey removeKey(@RequestBody IdempotenceKey idempotenceKey) {
        return redisConnector.removeKey(idempotenceKey.getShard(), idempotenceKey.getKey());
    }

}
