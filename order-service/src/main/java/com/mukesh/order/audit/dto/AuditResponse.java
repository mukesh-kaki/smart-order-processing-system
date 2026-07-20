package com.mukesh.order.audit.dto;

import com.mukesh.order.audit.enums.AuditAction;
import com.mukesh.order.audit.enums.AuditStatus;

import java.time.Instant;
import java.util.UUID;

public record AuditResponse(

        UUID id,

        UUID userId,

        String username,

        AuditAction action,

        AuditStatus status,

        String entityType,

        String entityId,

        String message,

        String deviceName,

        String ipAddress,

        Instant createdAt

) {
}