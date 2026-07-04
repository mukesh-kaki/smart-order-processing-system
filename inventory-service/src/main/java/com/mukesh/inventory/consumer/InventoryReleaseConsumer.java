package com.mukesh.inventory.consumer;

import com.mukesh.commonoutbox.idempotency.AbstractIdempotentConsumer;
import com.mukesh.commonoutbox.service.ProcessedEventService;
import com.mukesh.events.InventoryReleaseEvent;
import com.mukesh.inventory.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class InventoryReleaseConsumer extends AbstractIdempotentConsumer<InventoryReleaseEvent> {

    public InventoryReleaseConsumer(InventoryService inventoryService, ProcessedEventService processedEventService){
        super(processedEventService);

        this.inventoryService=inventoryService;
    }
    private final InventoryService inventoryService;

    @KafkaListener(topics = "${app.kafka.topics.mappings.InventoryReleaseEvent}")
    public void consume(InventoryReleaseEvent event){
        log.info(
                "Received InventoryReleaseEvent for Product {}",
                event.productId()
        );

        process(event);
    }
    @Override
    protected void handle(InventoryReleaseEvent event){
        inventoryService.releaseInventory(event);
    }



}
