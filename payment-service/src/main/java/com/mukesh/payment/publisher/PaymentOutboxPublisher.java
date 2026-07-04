package com.mukesh.payment.publisher;

import com.mukesh.commonoutbox.entity.EventStatus;
import com.mukesh.commonoutbox.entity.OutboxEntity;
import com.mukesh.commonoutbox.repository.OutboxRepository;
import com.mukesh.payment.config.KafkaTopicProperties;
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
public class PaymentOutboxPublisher {
    private final OutboxRepository outboxRepository;
    private final KafkaTemplate kafkaTemplate;
    private final KafkaTopicProperties kafkaTopicProperties;

    @Scheduled(fixedDelay = 5000)
    public void publishEvent() {
        List<OutboxEntity> events = outboxRepository.findByStatus(EventStatus.NEW);
        if (events.isEmpty()) {
            return;
        }
        log.info("Found {} payment events to publish.", events.size());
        for (OutboxEntity event : events) {
            try {
                publish(event);
            } catch (InterruptedException ex) {

                Thread.currentThread().interrupt();

                log.error(
                        "Interrupted while publishing {}",
                        event.getId(),
                        ex
                );

            } catch (ExecutionException ex) {

                log.error(
                        "Failed to publish {}",
                        event.getId(),
                        ex
                );
            }
        }
    }
        private void publish (OutboxEntity event) throws InterruptedException, ExecutionException {
        String topic=kafkaTopicProperties.getTopic(event.getType());
        kafkaTemplate.send(topic, event.getAggregateId(), event.getPayload()).get();

        event.setStatus(EventStatus.SENT);
        event.setPublishedAt(Instant.now());
        outboxRepository.save(event);
            log.info(
                    "Published event {} successfully.",
                    event.getId()
            );

        }

}
