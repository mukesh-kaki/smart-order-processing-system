package com.mukesh.inventory.publisher;

import com.mukesh.events.InventoryReleasedEvent;
import com.mukesh.events.InventoryReservedEvent;

import java.util.UUID;

public interface InventoryEventPublisher {

    void publishInventoryReserved(
            UUID orderId,
            InventoryReservedEvent event
    );

    void publishInventoryReleased(
            UUID orderId,
            InventoryReleasedEvent event
    );

}