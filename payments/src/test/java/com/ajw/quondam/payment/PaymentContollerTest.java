package com.ajw.quondam.payment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIn.isIn;
import static org.hamcrest.core.Every.everyItem;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class    PaymentContollerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    private PaymentService paymentService;

    @Autowired
    private TestRestTemplate restTemplate;

    private final Payment payment = new Payment("idempotenceKey", "cardNumber","value");

    @Test
    public void canAddKey() throws JsonProcessingException {
        // given
        given(paymentService.makePayment(payment))
                .willReturn(payment);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(payment), headers);

        // when
        ResponseEntity<Payment> response = restTemplate.exchange("/payments", HttpMethod.PUT, entity, Payment.class);

        // then
        assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));
    }

    @Test
    public void canTrackPayments() throws JsonProcessingException {
        // given
        given(paymentService.getNumberOfPayments())
                .willReturn(7L);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // when
        ResponseEntity<Long> response = restTemplate.getForEntity("/track", Long.class);

        // then
        assertThat(response.getBody(), equalTo(7L));
    }

    @Test
    public void canResetTracking() throws JsonProcessingException {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // when
        ResponseEntity<Object> response = restTemplate.getForEntity("/reset", Object.class);

        // then
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));

    }

    @Test
    public void canGetDuplicatePayments() throws JsonProcessingException {
        // given
        given(paymentService.getDuplicatePayments())
                .willReturn(Arrays.asList(payment));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // when
        ResponseEntity<Object[]>  response = restTemplate.getForEntity("/duplicates", Object[].class);

        Object[] duplicates = response.getBody();

        Map<String, String> expectedResult = new LinkedHashMap<>();

        expectedResult.put("idempotenceKey", "idempotenceKey");
        expectedResult.put("cardNumber", "cardNumber");
        expectedResult.put("value", "value");


        Map<String, String> paymentMap = ((LinkedHashMap)duplicates[0]);

        // then
        assertThat(expectedResult.entrySet(), everyItem(isIn(paymentMap.entrySet())));
    }

    @Test
    public void givenBuilder_whenDeserializing_thenCorrect()
            throws IOException {

        String json = "{\"idempotenceKey\":\"idempotenceKey\",\"cardNumber\":\"cardNumber\", \"value\":\"value\"}";
        ObjectMapper mapper = new ObjectMapper();

        Payment payment = mapper.reader()
                .forType(Payment.class).readValue(json);
        assertThat("idempotenceKey", equalTo(payment.getIdempotenceKey()));
        assertThat("cardNumber", equalTo(payment.getCardNumber()));
        assertThat("value", equalTo(payment.getValue()));
    }

}
