package com.mukesh.inventory.publisher;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.w3c.dom.events.Event;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Slf4j
@Component
@RequiredArgsConstructor
public class InventoryOutboxPublisher {

    private final OutboxRepository outboxRepository;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private final KafkaTopicProperties kafkaTopicProperties;

    @Scheduled(fixedDelay = 5000)
    public void publishEvents(){
        List<OutboxEntity> events= outboxRepository.findByStatus(EventStatus.NEW);
        if (events.isEmpty()) {
            return;
        }
        log.info("Found {} unpublished events", events.size());


        for(OutboxEntity event: events){
            try{
                publish(event);

            }catch(InterruptedException  ex){
                Thread.currentThread().interrupt();
                log.error("Failed to publish event {}", event.getId(), ex);
            }catch(ExecutionException ex){
                log.error("Kafka publish failed for event {}", event.getId(), ex);
            }
        }

    }

    private void publish(OutboxEntity event){
        String topic= kafkaTopicProperties.getTopic(event.getType());

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
