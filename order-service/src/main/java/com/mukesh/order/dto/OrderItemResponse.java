package com.mukesh.order.dto;

import java.util.UUID;

public record OrderItemResponse(

        UUID productId,

        Integer quantity

) {
}