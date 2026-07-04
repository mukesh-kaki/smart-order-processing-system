package com.mukesh.events;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderItem(
        UUID productId,
        String productName,
        Integer quantity,
        BigDecimal unitPrice
) {
}
