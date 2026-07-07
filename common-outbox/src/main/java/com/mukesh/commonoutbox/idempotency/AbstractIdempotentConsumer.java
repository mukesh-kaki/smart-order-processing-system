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

        log.info(
                "Processing {} with EventId={}",
                event.eventType(),
                event.eventId()
        );

        if (processedEventService.alreadyProcessed(event.eventId())) {

            log.info(
                    "Duplicate event ignored. EventType={} EventId={}",
                    event.eventType(),
                    event.eventId()
            );

            return;
        }

        try {

            handle(event);

            processedEventService.markProcessed(
                    event,
                    getClass()
            );

            log.info(
                    "Successfully processed {} EventId={}",
                    event.eventType(),
                    event.eventId()
            );

        } catch (Exception ex) {

            log.error(
                    "Failed processing {} EventId={}",
                    event.eventType(),
                    event.eventId(),
                    ex
            );

            /*
             * Very important:
             *
             * We DO NOT mark the event as processed.
             *
             * Kafka Retry / DLQ will handle this message.
             */

            throw ex;
        }

    }

    protected abstract void handle(T event);

}