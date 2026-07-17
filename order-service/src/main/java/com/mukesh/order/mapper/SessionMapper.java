package com.mukesh.order.mapper;

import com.mukesh.order.dto.SessionResponse;
import com.mukesh.order.entity.security.RefreshToken;

public final class SessionMapper {

    private SessionMapper() {
    }

    public static SessionResponse toResponse(
            RefreshToken token,
            String currentDeviceId
    ) {

        return new SessionResponse(

                token.getId(),

                token.getDeviceName(),

                token.getUserAgent(),

                token.getIpAddress(),

                token.getDeviceId(),

                token.getCreatedAt(),

                token.getLastUsedAt(),

                token.getExpiryDate(),

                token.getRevokedAt(),

                token.isRevoked(),

                token.getDeviceId().equals(currentDeviceId)

        );

    }

}