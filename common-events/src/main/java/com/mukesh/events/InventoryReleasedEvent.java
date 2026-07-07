package com.mukesh.events;

import java.time.Instant;
import java.util.UUID;

public record InventoryReleasedEvent(UUID eventId,

                                     UUID orderId,

                                     UUID productId,

                                     int quantity,

                                     Instant releasedAt)implements DomainEvent {
}
