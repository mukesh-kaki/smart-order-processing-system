package com.mukesh.order.consumer;

import com.mukesh.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import com.mukesh.events.InventoryReservedEvent;

@Slf4j
@Component
@RequiredArgsConstructor
public class InventoryReservedConsumer {
    private final OrderService orderService;

    @KafkaListener(topics = "${ app.kafka.topics.mappings.InventoryReservedEvent}")

    public void consume(InventoryReservedEvent event){
        log.info("Received InventoryReservedEvent : {}", event);
        orderService.handleInventoryReserved(event);
    }
}
