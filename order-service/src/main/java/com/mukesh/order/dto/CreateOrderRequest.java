package com.mukesh.order.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record CreateOrderRequest(

        @NotNull
        UUID customerId,

        @NotBlank
        String currency,

        @NotEmpty
        @Valid
        List<OrderItemRequest> items

) {
}