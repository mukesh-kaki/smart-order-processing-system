package com.mukesh.order.audit.service;

import com.mukesh.order.audit.constants.EntityType;
import com.mukesh.order.audit.dto.AuditRequest;
import com.mukesh.order.audit.entity.AuditLog;
import com.mukesh.order.audit.enums.AuditAction;
import com.mukesh.order.audit.enums.AuditStatus;
import com.mukesh.order.audit.repository.AuditRepository;
import com.mukesh.order.dto.SessionInfo;
import com.mukesh.order.entity.AppUser;
import com.mukesh.order.entity.security.RefreshToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuditService {

    private final AuditRepository repository;

    /**
     * Generic audit logger.
     * Can be used by any module.
     */
    public void log(AuditRequest request) {

        AuditLog auditLog = AuditLog.builder()
                .userId(request.userId())
                .username(request.username())
                .action(request.action())
                .status(request.status())
                .entityType(request.entityType())
                .entityId(request.entityId())
                .message(request.message())
                .deviceName(request.deviceName())
                .ipAddress(request.ipAddress())
                .build();

        repository.save(auditLog);
    }

    /**
     * Successful login.
     */
    public void logLoginSuccess(
            AppUser user,
            SessionInfo sessionInfo
    ) {

        log(
                new AuditRequest(
                        user.getId(),
                        user.getUserName(),
                        AuditAction.LOGIN_SUCCESS,
                        AuditStatus.SUCCESS,
                        EntityType.AUTHENTICATION,
                        user.getId().toString(),
                        "User logged in successfully.",
                        sessionInfo.deviceName(),
                        sessionInfo.ipAddress()
                )
        );

    }

    /**
     * Failed login.
     */
    public void logLoginFailure(
            String username,
            SessionInfo sessionInfo
    ) {

        log(
                new AuditRequest(
                        null,
                        username,
                        AuditAction.LOGIN_FAILED,
                        AuditStatus.FAILURE,
                        EntityType.AUTHENTICATION,
                        null,
                        "Invalid username or password.",
                        sessionInfo.deviceName(),
                        sessionInfo.ipAddress()
                )
        );

    }

    /**
     * JWT refresh.
     */
    public void logTokenRefresh(
            AppUser user,
            SessionInfo sessionInfo
    ) {

        log(
                new AuditRequest(
                        user.getId(),
                        user.getUserName(),
                        AuditAction.TOKEN_REFRESHED,
                        AuditStatus.SUCCESS,
                        EntityType.AUTHENTICATION,
                        user.getId().toString(),
                        "Access token refreshed.",
                        sessionInfo.deviceName(),
                        sessionInfo.ipAddress()
                )
        );

    }

    /**
     * Logout current session.
     */
    public void logLogout(
            RefreshToken token
    ) {

        log(
                new AuditRequest(
                        token.getUser().getId(),
                        token.getUser().getUserName(),
                        AuditAction.LOGOUT,
                        AuditStatus.SUCCESS,
                        EntityType.AUTHENTICATION,
                        token.getId().toString(),
                        "User logged out.",
                        token.getDeviceName(),
                        token.getIpAddress()
                )
        );

    }

    /**
     * Logout all sessions.
     */
    public void logLogoutAll(
            AppUser user
    ) {

        log(
                new AuditRequest(
                        user.getId(),
                        user.getUserName(),
                        AuditAction.LOGOUT_ALL,
                        AuditStatus.SUCCESS,
                        EntityType.AUTHENTICATION,
                        user.getId().toString(),
                        "All active sessions revoked.",
                        null,
                        null
                )
        );

    }

    /**
     * Session revoked by user/admin.
     */
    public void logSessionRevoked(
            AppUser user,
            UUID sessionId
    ) {

        log(
                new AuditRequest(
                        user.getId(),
                        user.getUserName(),
                        AuditAction.SESSION_REVOKED,
                        AuditStatus.SUCCESS,
                        EntityType.AUTHENTICATION,
                        sessionId.toString(),
                        "Session revoked.",
                        null,
                        null
                )
        );

    }

    /**
     * Generic success event.
     * Useful for Order, Inventory and Payment modules.
     */
    public void logSuccess(
            AppUser user,
            AuditAction action,
            String entityType,
            String entityId,
            String message
    ) {

        log(
                new AuditRequest(
                        user.getId(),
                        user.getUserName(),
                        action,
                        AuditStatus.SUCCESS,
                        entityType,
                        entityId,
                        message,
                        null,
                        null
                )
        );

    }

    /**
     * Generic failure event.
     */
    public void logFailure(
            AppUser user,
            AuditAction action,
            String entityType,
            String entityId,
            String message
    ) {

        log(
                new AuditRequest(
                        user.getId(),
                        user.getUserName(),
                        action,
                        AuditStatus.FAILURE,
                        entityType,
                        entityId,
                        message,
                        null,
                        null
                )
        );

    }

}