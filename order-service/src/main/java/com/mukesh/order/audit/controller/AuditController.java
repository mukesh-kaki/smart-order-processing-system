package com.mukesh.order.audit.controller;

import com.mukesh.order.audit.dto.AuditResponse;
import com.mukesh.order.audit.enums.AuditAction;
import com.mukesh.order.audit.enums.AuditStatus;
import com.mukesh.order.audit.service.AuditService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/audit")
@RequiredArgsConstructor
@Validated
@PreAuthorize("hasRole('ADMIN')")
public class AuditController {

    private final AuditService auditService;

    @GetMapping
    public Page<AuditResponse> getAllAuditLogs(
            Pageable pageable
    ) {

        return auditService.getAll(pageable);

    }

    @GetMapping("/users/{username}")
    public Page<AuditResponse> getAuditLogsByUsername(@PathVariable @NotBlank String username,
            Pageable pageable
    ) {

        return auditService.getByUsername(
                username,
                pageable
        );

    }

    @GetMapping("/actions/{action}")
    public Page<AuditResponse> getAuditLogsByAction(
            @PathVariable AuditAction action,
            Pageable pageable
    ) {

        return auditService.getByAction(
                action,
                pageable
        );

    }

    @GetMapping("/status/{status}")
    public Page<AuditResponse> getAuditLogsByStatus(
            @PathVariable AuditStatus status,
            Pageable pageable
    ) {

        return auditService.getByStatus(
                status,
                pageable
        );

    }

}