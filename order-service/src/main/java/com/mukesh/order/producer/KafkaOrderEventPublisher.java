package com.mukesh.order.producer;

import com.mukesh.events.OrderCreatedEvent;
import com.mukesh.order.config.KafkaTopicProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaOrderEventPublisher implements OrderEventPublisher {

    private final KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;

    private final KafkaTopicProperties kafkaTopicProperties;

    @Override
    public void publishOrderCreated(OrderCreatedEvent event) {

        kafkaTemplate.send(
                kafkaTopicProperties.getTopic("OrderCreatedEvent"),
                event.orderId().toString(),
                event
        );

        log.info("Publishing OrderCreatedEvent : {}", event.orderId());
    }
}
