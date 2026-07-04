package com.mukesh.commonoutbox.idempotency;

import com.mukesh.commonoutbox.idempotency.service.ProcessedEventService;
import com.mukesh.events.DomainEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractIdempotentConsumer<T extends DomainEvent> {

    private final ProcessedEventService processedEventService;

    public final void process(T event) {

        if (processedEventService.alreadyProcessed(event.eventId())) {

            log.info(
                    "Ignoring duplicate {}. EventId={}",
                    event.eventType(),
                    event.eventId()
            );

            return;
        }

        handle(event);

    }

    protected abstract void handle(T event);

}