package com.mukesh.order.producer;

import com.mukesh.order.config.KafkaTopicProperties;
import com.mukesh.events.OrderCreatedEvent;

@Slf4j
@Component
public class KafkaOrderEventPublisher implements OrderEventPublisher{

    private final KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;

    private final KafkaTopicProperties kafkaTopicProperties;

    @Override
    public void publishOrderCreated(OrderCreatedEvent event) {

        kafkaTemplate.send(kafkaTopicProperties.getOrderCreated(), event.orderId().toString(), event);

        log.info("Publishing OrderCreatedEvent : {}", event.orderId());

    }
}
