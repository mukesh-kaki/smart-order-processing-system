package com.mukesh.commonoutbox.service;

import com.mukesh.commonoutbox.entity.AggregateType;
import com.mukesh.commonoutbox.entity.OutboxEntity;
import com.mukesh.commonoutbox.factory.OutboxFactory;
import com.mukesh.events.DomainEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OutboxPublisherServiceImpl implements OutboxPublisherService {
    private final OutboxFactory outboxFactory;

    private final OutboxService outboxService;

    @Override
    public void publish(
            UUID aggregateId,
            AggregateType aggregateType,
            DomainEvent event
    ) {

        OutboxEntity outboxEntity =
                outboxFactory.create(
                        aggregateId,
                        aggregateType,
                        event
                );

        outboxService.save(outboxEntity);
    }

}
