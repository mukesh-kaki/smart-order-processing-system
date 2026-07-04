package com.mukesh.order.consumer;

import com.mukesh.events.PaymentFailedEvent;
import com.mukesh.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentFailedConsumer {

    private final OrderService orderService;

    @KafkaListener(
            topics = "${app.kafka.topics.mappings.PaymentFailedEvent}"
    )
    public void consume(PaymentFailedEvent event) {

        log.info(
                "Received PaymentFailedEvent for Order {}",
                event.orderId()
        );

        //orderService.handlePaymentFailed(event);
    }
    @Override
    protected void handle(PaymentFailedEvent event) {

        orderService.handlePaymentFailed(event);

    }
}