package com.mukesh.payment.service;

import com.mukesh.events.PaymentRequestedEvent;

public interface PaymentService {

    void processPayment(PaymentRequestedEvent event);

}
