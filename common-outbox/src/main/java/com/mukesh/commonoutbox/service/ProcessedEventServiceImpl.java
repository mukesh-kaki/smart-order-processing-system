package com.mukesh.commonoutbox.idempotency.service;

import com.mukesh.commonoutbox.idempotency.entity.ProcessedEventEntity;
import com.mukesh.commonoutbox.idempotency.repository.ProcessedEventRepository;
import com.mukesh.events.DomainEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProcessedEventServiceImpl
        implements ProcessedEventService {

    private final ProcessedEventRepository repository;

    @Override
    public boolean alreadyProcessed(UUID eventId) {

        return repository.existsByEventId(eventId);

    }

    @Override
    public void markProcessed(
            DomainEvent event,
            Class<?> consumerClass
    ) {

        repository.save(

                ProcessedEventEntity.builder()

                        .eventId(event.eventId())

                        .eventType(event.eventType())

                        .consumerName(
                                consumerClass.getSimpleName()
                        )

                        .processedAt(Instant.now())

                        .build()

        );

    }

}