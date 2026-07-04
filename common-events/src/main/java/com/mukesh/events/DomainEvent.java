package com.mukesh.events;

import java.util.UUID;

public interface DomainEvent {
    UUID eventId();

    default String eventType() {
        return getClass().getSimpleName();
    }
}
