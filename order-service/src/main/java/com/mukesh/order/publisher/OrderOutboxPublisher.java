package com.mukesh.order.publisher;

import com.mukesh.commonoutbox.entity.OutboxEntity;
import com.mukesh.commonoutbox.entity.EventStatus;
import com.mukesh.commonoutbox.repository.OutboxRepository;
import com.mukesh.events.DomainEvent;
import com.mukesh.order.config.KafkaTopicProperties;
import com.mukesh.order.producer.KafkaEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderOutboxPublisher {

    private final OutboxRepository outboxRepository;
    private final KafkaTopicProperties topicProperties;
    private final KafkaEventPublisher kafkaEventPublisher;

    @Scheduled(fixedDelayString = "${app.scheduler.outbox-delay}")
    public void publishEvents() {

        List<OutboxEntity> events =
                outboxRepository.findByStatus(EventStatus.NEW);

        if (events.isEmpty()) {
            return;
        }

        log.info("Found {} pending outbox events", events.size());

        for (OutboxEntity event : events) {

            try {

                String topic =
                        topicProperties.getTopic(event.getEventType());

                kafkaEventPublisher.publish(
                        topic,
                        event.getAggregateId().toString(),
                        (DomainEvent) event.getPayload()
                );

                event.setStatus(EventStatus.SENT);
                event.setPublishedAt(Instant.now());

                outboxRepository.save(event);

                log.info(
                        "Published eventId={} eventType={} aggregateId={} topic={}",
                        event.getId(),
                        event.getEventType(),
                        event.getAggregateId(),
                        topic
                );

            } catch (Exception ex) {

                log.error(
                        "Failed to publish eventId={} eventType={}",
                        event.getId(),
                        event.getEventType(),
                        ex
                );
            }
        }
    }

}
