package com.ajw.bond.controller;

import com.ajw.bond.model.IdempotenceKey;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class KeyControllerTest {

    @Autowired
    private KeyController keyController;

    @Test
    public void contextLoads() throws Exception {
        assertThat(keyController).isNotNull();
    }

    @Test
    public void putKey() throws Exception {
        IdempotenceKey testKey = new IdempotenceKey("testShard", "testKey");
        IdempotenceKey keyToAssert = keyController.putKey(testKey);
        assertThat(keyToAssert.getShard()).isEqualTo("testShard");
        assertThat(keyToAssert.getKey()).isEqualTo("testKey");
    }


    @Test
    public void removeKey() throws Exception {
        IdempotenceKey testKey = new IdempotenceKey("testShard", "testKey");

        keyController.putKey(testKey);

        IdempotenceKey keyToAssert = keyController.removeKey(testKey);
        assertThat(keyToAssert.getShard()).isEqualTo("testShard");
        assertThat(keyToAssert.getKey()).isEqualTo("testKey");
    }

}
