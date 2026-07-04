package com.mukesh.inventory.consumer;

import com.mukesh.events.OrderCreatedEvent;
import com.mukesh.inventory.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderCreatedConsumer {

    private final InventoryService inventoryService;

    @KafkaListener(
            topics = "${app.kafka.topics.mappings.InventoryReservedEvent}",
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