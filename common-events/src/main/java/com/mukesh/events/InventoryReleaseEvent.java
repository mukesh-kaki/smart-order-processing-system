package com.mukesh.events;

import java.time.Instant;
import java.util.UUID;

public record InventoryReleaseEvent(

        UUID eventId,

        UUID orderId,

        UUID productId,

        int quantity,

        Instant releasedRequestedAt

) implements DomainEvent {
}