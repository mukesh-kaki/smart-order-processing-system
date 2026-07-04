package com.mukesh.order.consumer;

import com.mukesh.events.PaymentCompletedEvent;
import com.mukesh.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentCompletedConsumer {
    private final OrderService orderService;

    @KafkaListener(topics="${app.kafka.topics.mappings.PaymentCompletedEvent}")

        public void consume(PaymentCompletedEvent event){
            log.info(
                    "Received PaymentCompletedEvent for Order {}",
                    event.orderId()
            );
            orderService.handlePaymentCompleted(event);
        }
}
