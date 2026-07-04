package com.mukesh.events;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record OrderCreatedEvent(
        UUID eventId,
        UUID orderId,
        UUID customerId,
        //Long productId,
        List<OrderItem> items,
        BigDecimal totalAmount,
        String currency,
        Instant createdAt

) implements DomainEvent{
}
