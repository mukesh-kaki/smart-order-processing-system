package com.mukesh.order.audit.repository;

import com.mukesh.order.audit.entity.AuditLog;
import com.mukesh.order.audit.enums.AuditAction;
import com.mukesh.order.audit.enums.AuditStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AuditRepository
        extends JpaRepository<AuditLog, UUID> {

    Page<AuditLog> findAll(Pageable pageable);

    Page<AuditLog> findByUsername(
            String username,
            Pageable pageable
    );

    Page<AuditLog> findByAction(
            AuditAction action,
            Pageable pageable
    );

    Page<AuditLog> findByStatus(
            AuditStatus status,
            Pageable pageable
    );

}