package com.mukesh.payment.service;

import com.mukesh.events.PaymentRequestedEvent;
import com.mukesh.payment.gateway.PaymentGateway;


public interface PaymentService {

    private final PaymentGateway paymentGateway;
    void processPayment(PaymentRequestedEvent event);

}
