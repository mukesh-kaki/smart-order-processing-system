package com.mukesh.order.dto;

import com.mukesh.order.entity.OrderStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record OrderResponse(

        UUID orderId,

        UUID customerId,

        BigDecimal totalAmount,

        String currency,

        OrderStatus status,

        List<OrderItemResponse> items,

        Instant createdAt

) {
}