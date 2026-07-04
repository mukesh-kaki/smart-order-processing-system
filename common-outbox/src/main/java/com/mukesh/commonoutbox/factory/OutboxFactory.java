package com.mukesh.commonoutbox.factory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mukesh.commonoutbox.entity.AggregateType;
import com.mukesh.commonoutbox.entity.EventStatus;
import com.mukesh.commonoutbox.entity.OutboxEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OutboxFactory {

    private final ObjectMapper objectMapper;

    public OutboxEntity create(
            UUID aggregateId,
            AggregateType aggregateType,
            Object event
    ) {

        try {

            String payload =
                    objectMapper.writeValueAsString(event);

            return OutboxEntity.builder()
                    .aggregateId(aggregateId)
                    .aggregateType(aggregateType.name())
                    .eventType(event.getClass().getSimpleName())
                    .payload(payload)
                    .status(EventStatus.NEW)
                    .createdAt(Instant.now())
                    .build();

        } catch (JsonProcessingException ex) {

            throw new RuntimeException(
                    "Unable to serialize event.",
                    ex
            );

        }

    }

}