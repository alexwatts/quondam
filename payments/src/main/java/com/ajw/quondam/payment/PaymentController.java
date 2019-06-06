package com.ajw.quondam.payment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
public class PaymentController {

    Logger logger = LoggerFactory.getLogger(PaymentController.class);

    @Autowired
    public PaymentService paymentService;

    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/payments", method = RequestMethod.PUT)
    public Payment putKey(@RequestBody Payment payment) {
        return paymentService.makePayment(payment);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/track", method = RequestMethod.GET)
    public Long track() {
        return paymentService.getNumberOfPayments();
    }

    @RequestMapping(value="/duplicates", method=RequestMethod.GET)
    public @ResponseBody List<Object> duplicates() {
        return Arrays.asList(paymentService.getDuplicatePayments().toArray());
    }

    @RequestMapping(value="/reset", method=RequestMethod.GET)
    public void reset() {
        paymentService.resetTracking();
    }
}
