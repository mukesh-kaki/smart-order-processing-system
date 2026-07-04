package com.mukesh.commonoutbox.repository;

import com.mukesh.commonoutbox.entity.EventStatus;
import com.mukesh.commonoutbox.entity.OutboxEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface OutboxRepository
        extends JpaRepository<OutboxEntity, UUID> {

    List<OutboxEntity> findByStatus(EventStatus status);

    List<OutboxEntity> findByStatusOrderByCreatedAtAsc(
            EventStatus status,
            Pageable pageable
    );

}