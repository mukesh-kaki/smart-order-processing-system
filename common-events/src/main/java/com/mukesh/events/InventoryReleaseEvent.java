package com.mukesh.events;

import java.time.Instant;
import java.util.UUID;

public record InventoryReleaseEvent( UUID eventId,
        UUID orderId,

                                    UUID customerId,

                                    UUID productId,

                                    Integer quantity,

                                    Instant releasedAt)implements DomainEvent {
}
