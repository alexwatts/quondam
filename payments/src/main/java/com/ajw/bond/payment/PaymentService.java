package com.ajw.bond.payment;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    private final Map<Payment, LongAdder> paymentTracker = new ConcurrentHashMap<>();

    public Payment makePayment(Payment payment) {
        paymentTracker.putIfAbsent(payment, new LongAdder());
        paymentTracker.get(payment).increment();
        return payment;
    }

    public Long getNumberOfPayments() {

        Optional<Long> payments = paymentTracker.
                keySet().
                stream().
                map(paymentTracker::get).map(LongAdder::longValue).reduce(Long::sum);

        return payments.orElse(0L);
    }

    public List<Payment> getDuplicatePayments() {
        return paymentTracker.
                keySet().
                stream().
                filter(payment -> paymentTracker.get(payment).longValue() > 1).
                collect(Collectors.toList());
    }

    public void resetTracking() {
        paymentTracker.clear();
    }
}
