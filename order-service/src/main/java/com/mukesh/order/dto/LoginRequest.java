package com.mukesh.order.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


public record LoginRequest(
        @NotBlank
        String username,

        @NotBlank
        String password
) {
}
