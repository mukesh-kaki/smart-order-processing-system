package com.mukesh.order.dto;

public record LoginResponse(
        String accessToken,

        String refreshToken,

        String tokenType,

        long expiresIn

) {
}
