package com.mukesh.commonoutbox.idempotency.service;

import com.mukesh.events.DomainEvent;

import java.util.UUID;

public interface ProcessedEventService {

    boolean alreadyProcessed(UUID eventId);

    void markProcessed(
            DomainEvent event,
            Class<?> consumerClass
    );
}
