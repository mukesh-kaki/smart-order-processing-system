package com.mukesh.order.audit.mapper;

import com.mukesh.order.audit.dto.AuditResponse;
import com.mukesh.order.audit.entity.AuditLog;

public final class AuditMapper {

    private AuditMapper() {
    }

    public static AuditResponse toResponse(AuditLog auditLog) {

        return new AuditResponse(

                auditLog.getId(),

                auditLog.getUserId(),

                auditLog.getUsername(),

                auditLog.getAction(),

                auditLog.getStatus(),

                auditLog.getEntityType(),

                auditLog.getEntityId(),

                auditLog.getMessage(),

                auditLog.getDeviceName(),

                auditLog.getIpAddress(),

                auditLog.getCreatedAt()
        );
    }

}