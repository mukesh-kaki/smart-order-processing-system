package com.mukesh.commonoutbox.service;

import com.mukesh.commonoutbox.entity.AggregateType;
import com.mukesh.events.DomainEvent;

import java.util.UUID;

/**
 * Publishes domain events by storing them in the transactional outbox.
 *
 * Business services should depend on this abstraction instead of
 * directly interacting with the outbox infrastructure.
 */

public interface OutboxPublisherService {

    /**
     * Stores a domain event in the outbox table.
     *
     * @param aggregateId aggregate identifier
     * @param aggregateType aggregate type
     * @param event domain event
     */
    void publish(
            UUID aggregateId,
            AggregateType aggregateType,
            DomainEvent event
    );
}
