package com.mukesh.commonoutbox.idempotency.repository;

import com.mukesh.commonoutbox.entity.ProcessedEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProcessedEventRepository
        extends JpaRepository<ProcessedEventEntity, UUID> {

    boolean existsByEventId(UUID eventId);

}
