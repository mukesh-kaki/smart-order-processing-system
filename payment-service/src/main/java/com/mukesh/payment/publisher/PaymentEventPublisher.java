package com.mukesh.payment.publisher;

import com.mukesh.events.PaymentCompletedEvent;
import com.mukesh.events.PaymentFailedEvent;

import java.util.UUID;

public interface PaymentEventPublisher {

    void publishPaymentCompleted(
            UUID paymentId,
            PaymentCompletedEvent event
    );

    void publishPaymentFailed(
            UUID paymentId,
            PaymentFailedEvent event
    );

}