package com.mukesh.order.producer;

import com.mukesh.events.DomainEvent;
import com.mukesh.order.exception.KafkaPublishException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaEventPublisherImpl implements KafkaEventPublisher{

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void publish(
            String topic,
            String key,
            String payload) {

        try {

            SendResult<String, Object> result =
                    kafkaTemplate
                            .send(topic, key, payload)
                            .get();

            log.info(
                    "Kafka publish successful | topic={} partition={} offset={} eventType={}",
                    result.getRecordMetadata().topic(),
                    result.getRecordMetadata().partition(),
                    result.getRecordMetadata().offset()
            );

        } catch (InterruptedException ex) {

            Thread.currentThread().interrupt();

            throw new KafkaPublishException(
                    "Interrupted while publishing event to Kafka",
                    ex
            );

        } catch (ExecutionException ex) {

            throw new KafkaPublishException(
                    "Failed to publish event to Kafka",
                    ex
            );
        }
    }


}
