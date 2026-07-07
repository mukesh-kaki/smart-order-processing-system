package com.mukesh.order.consumer;

import com.mukesh.commonoutbox.idempotency.AbstractIdempotentConsumer;
import com.mukesh.commonoutbox.idempotency.service.ProcessedEventService;
import com.mukesh.events.PaymentFailedEvent;
import com.mukesh.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PaymentFailedConsumer
        extends AbstractIdempotentConsumer<PaymentFailedEvent> {

    private final OrderService orderService;

    public PaymentFailedConsumer(
            OrderService orderService,
            ProcessedEventService processedEventService
    ) {
        super(processedEventService);
        this.orderService = orderService;
    }

    @KafkaListener(
            topics = "${app.kafka.topics.mappings.PaymentFailedEvent}"
    )
    public void consume(PaymentFailedEvent event) {

        log.info(
                "Received PaymentFailedEvent for Order {}",
                event.orderId()
        );

        process(event);
    }

    @Override
    protected void handle(PaymentFailedEvent event) {
        orderService.handlePaymentFailed(event);
    }
}