package com.mukesh.inventory.consumer;

import com.mukesh.commonoutbox.idempotency.AbstractIdempotentConsumer;
import com.mukesh.commonoutbox.idempotency.service.ProcessedEventService;
import com.mukesh.events.OrderCreatedEvent;
import com.mukesh.inventory.service.InventoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderCreatedConsumer
        extends AbstractIdempotentConsumer<OrderCreatedEvent> {

    private final InventoryService inventoryService;

    public OrderCreatedConsumer(
            InventoryService inventoryService,
            ProcessedEventService processedEventService
    ) {
        super(processedEventService);
        this.inventoryService = inventoryService;
    }

    @KafkaListener(
            topics = "${app.kafka.topics.mappings.OrderCreatedEvent}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consume(OrderCreatedEvent event) {

        log.info(
                "Received OrderCreatedEvent for Order {}",
                event.orderId()
        );

        process(event);
    }

    @Override
    protected void handle(OrderCreatedEvent event) {

        inventoryService.reserveInventory(event);

        log.info(
                "Inventory reserved successfully for Order {}",
                event.orderId()
        );
    }
}