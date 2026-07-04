package com.mukesh.events;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record InventoryReservedEvent
        ( UUID eventId,
                UUID orderId,
         UUID customerId,
         List<OrderItem> items,
         Instant reservedAt)implements DomainEvent{

}
