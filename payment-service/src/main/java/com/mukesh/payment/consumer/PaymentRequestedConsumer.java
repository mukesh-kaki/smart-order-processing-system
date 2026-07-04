package com.mukesh.payment.consumer;

import com.mukesh.events.PaymentRequestedEvent;
import com.mukesh.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentRequestedConsumer {

    //private static final Logger log = LoggerFactory.getLogger(PaymentRequestedConsumer.class);
    private final PaymentService paymentService;

    @KafkaListener(topics ="$ {app.kafka.topics.mappings.PaymentRequestedEvent}")
    public void consume(PaymentRequestedEvent event){
        log.info("Received Payment Request Event for Order {}", event.orderId());

        paymentService.processPayment(event);
    }
}
