package com.mukesh.order.dto;

import java.time.Instant;
import java.util.UUID;

public record SessionResponse(

        UUID id,

        String deviceName,

        String userAgent,

        String ipAddress,

        String deviceId,

        Instant createdAt,

        Instant lastUsedAt,

        Instant expiryDate,

        Instant revokedAt,

        boolean revoked,

        boolean currentSession

) {
}