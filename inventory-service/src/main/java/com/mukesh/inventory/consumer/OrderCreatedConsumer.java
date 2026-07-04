package com.mukesh.inventory.consumer;

import com.mukesh.events.OrderCreatedEvent;
import com.mukesh.inventory.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderCreatedConsumer {

    private final InventoryService inventoryService;

    @KafkaListener(
            topics = "${app.kafka.topics.mappings.OrderCreatedEvent}"
    )
    public void consume(OrderCreatedEvent event) {

        log.info("Received OrderCreatedEvent : {}", event);

        inventoryService.reserveInventory(event);

        log.info(
                "Inventory reserved successfully : {}",
                event.orderId()
        );
    }

}
