package com.ajw.bond.controller;

import com.ajw.bond.model.IdempotenceKey;
import com.ajw.bond.redis.RedisConnector;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class KeyControllerIntegrationTest {

    private ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    private RedisConnector redisConnector;

    @Autowired
    private TestRestTemplate restTemplate;

    private final IdempotenceKey idempotenceKey = new IdempotenceKey("testShard", "testKey", Boolean.FALSE);

    @Test
    public void canAddKey() throws JsonProcessingException {
        // given
        given(redisConnector.putKey("testShard", "testKey"))
                .willReturn(idempotenceKey);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(idempotenceKey), headers);

        // when
        ResponseEntity<IdempotenceKey> response = restTemplate.exchange("/keys", HttpMethod.PUT, entity, IdempotenceKey.class);

        // then
        assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));
    }

    @Test
    public void canRemoveKey() throws JsonProcessingException {
        // given
        given(redisConnector.putKey("testShard", "testKey"))
                .willReturn(idempotenceKey);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(idempotenceKey), headers);

        // when
        ResponseEntity<IdempotenceKey> response = restTemplate.exchange("/keys", HttpMethod.DELETE, entity, IdempotenceKey.class);

        // then
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
    }

}