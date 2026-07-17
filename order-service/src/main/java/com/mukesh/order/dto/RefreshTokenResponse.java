package com.mukesh.order.dto;

public record RefreshTokenResponse(String accessToken,

                                   String refreshToken,

                                   String tokenType,

                                   long expiresIn) {
}
