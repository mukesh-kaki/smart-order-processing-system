package com.mukesh.payment.consumer;

import com.mukesh.commonoutbox.idempotency.AbstractIdempotentConsumer;
import com.mukesh.commonoutbox.idempotency.service.ProcessedEventService;
import com.mukesh.events.PaymentRequestedEvent;
import com.mukesh.payment.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PaymentRequestedConsumer
        extends AbstractIdempotentConsumer<PaymentRequestedEvent> {

    private final PaymentService paymentService;

    public PaymentRequestedConsumer(
            PaymentService paymentService,
            ProcessedEventService processedEventService
    ) {
        super(processedEventService);
        this.paymentService = paymentService;
    }

    @KafkaListener(
            topics = "${app.kafka.topics.mappings.PaymentRequestedEvent}"
    )
    public void consume(PaymentRequestedEvent event) {

        log.info(
                "Received PaymentRequestedEvent for Order {}",
                event.orderId()
        );

        process(event);
    }

    @Override
    protected void handle(PaymentRequestedEvent event) {
        paymentService.processPayment(event);
    }
}