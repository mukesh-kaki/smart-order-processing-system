package com.mukesh.inventory.publisher;

import com.mukesh.commonoutbox.entity.AggregateType;
import com.mukesh.commonoutbox.service.OutboxPublisherService;
import com.mukesh.events.InventoryReleasedEvent;
import com.mukesh.events.InventoryReservedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class InventoryEventPublisherImpl implements InventoryEventPublisher {

    private final OutboxPublisherService outboxPublisherService;

    @Override
    public void publishInventoryReserved(
            UUID orderId,
            InventoryReservedEvent event) {

        outboxPublisherService.publish(
                orderId,
                AggregateType.INVENTORY,
                event
        );

        log.info(
                "Published InventoryReservedEvent for Order {}",
                orderId
        );
    }

    @Override
    public void publishInventoryReleased(
            UUID orderId,
            InventoryReleasedEvent event) {

        outboxPublisherService.publish(
                orderId,
                AggregateType.INVENTORY,
                event
        );

        log.info(
                "Published InventoryReleasedEvent for Order {}",
                orderId
        );
    }
}