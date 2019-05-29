package com.ajw.bond.controller;

import com.ajw.bond.model.IdempotenceKey;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

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
        IdempotenceKey testKey = new IdempotenceKey("testShard", "testKey", Boolean.FALSE);
        IdempotenceKey keyToAssert = keyController.putKey(testKey);
        assertThat(keyToAssert.getShard()).isEqualTo("testShard");
        assertThat(keyToAssert.getKey()).isEqualTo("testKey");
        assertThat(keyToAssert.getClaimed()).isEqualTo(false);
    }


    @Test
    public void removeKey() throws Exception {
        IdempotenceKey testKey = new IdempotenceKey("testShard", "testKey", Boolean.FALSE);

        keyController.putKey(testKey);

        IdempotenceKey keyToAssert = keyController.removeKey(testKey);
        assertThat(keyToAssert.getShard()).isEqualTo("testShard");
        assertThat(keyToAssert.getKey()).isEqualTo("testKey");
        assertThat(keyToAssert.getClaimed()).isEqualTo(true);
    }

    @Test
    public void givenBuilder_whenDeserializing_thenCorrect()
            throws IOException {

        String json = "{\"shard\":\"shard\",\"key\":\"key\", \"claimed\":true}";
        ObjectMapper mapper = new ObjectMapper();

        IdempotenceKey key = mapper.reader()
                .forType(IdempotenceKey.class).readValue(json);
        assertThat("key").isEqualTo(key.getKey());
        assertThat("shard").isEqualTo(key.getShard());
        assertThat(true).isEqualTo(key.getClaimed());
    }

}
