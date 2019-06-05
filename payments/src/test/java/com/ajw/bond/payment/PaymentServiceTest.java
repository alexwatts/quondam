package com.ajw.bond.payment;

import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class PaymentServiceTest {

    private PaymentService paymentGateway;

    @Before
    public void setUp() {
        paymentGateway = new PaymentService();
    }

    @Test
    public void shouldBeAbleToMakeAPayment() {
        Payment payment = new Payment("idempotenceKey", "cardnumber", "value");
        paymentGateway.makePayment(payment);
        assertThat(payment.isPaid(), equalTo(Boolean.TRUE));
    }

    @Test
    public void shouldBeAbleToTrackPayments() {
        Payment payment = new Payment("idempotenceKey", "cardnumber", "value");
        paymentGateway.makePayment(payment);
        paymentGateway.makePayment(payment);
        paymentGateway.makePayment(payment);
        paymentGateway.makePayment(payment);
        assertThat(paymentGateway.getNumberOfPayments(), equalTo(4L));
    }

    @Test
    public void shouldBeAbleToListPaymentDuplicates() {
        Payment payment = new Payment("idempotenceKey", "cardnumber", "value");
        Payment duplicatePayment = new Payment("idempotenceKey", "cardnumber", "value");
        paymentGateway.makePayment(payment);
        paymentGateway.makePayment(duplicatePayment);
        assertThat(paymentGateway.getDuplicatePayments(), equalTo(Arrays.asList(duplicatePayment)));
    }

    @Test
    public void shouldNotFindDupllicatesWhenNoneExist() {
        Payment payment = new Payment("idempotenceKey", "cardnumber", "value");
        paymentGateway.makePayment(payment);
        assertThat(paymentGateway.getDuplicatePayments(), equalTo(Lists.emptyList()));
    }

    @Test
    public void shouldBeAbleToResetTrackingt() {
        Payment payment = new Payment("idempotenceKey", "cardnumber", "value");
        paymentGateway.makePayment(payment);
        paymentGateway.makePayment(payment);
        paymentGateway.makePayment(payment);
        paymentGateway.makePayment(payment);

        assertThat(paymentGateway.getNumberOfPayments(), equalTo(4L));

        paymentGateway.resetTracking();
        assertThat(paymentGateway.getNumberOfPayments(), equalTo(0L));
    }

}
