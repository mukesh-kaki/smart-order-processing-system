package com.mukesh.order.config;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

public class KafkaErrorHandlerConfig {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Bean
    public DefaultErrorHandler kafkaErrorHandler(){
        DeadLetterPublishingRecoverer recoverer=new DeadLetterPublishingRecoverer(kafkaTemplate, this::resolveDeadLetterTopic);

        FixedBackOff backOff=new FixedBackOff(2000L,3L);

        return new DefaultErrorHandler(recoverer, backOff);
    }

    private TopicPartition resolveDeadLetterTopic(ConsumerRecord<?, ?>record, Exception exception){
        String deadLetterTopic=record.topic()+"-dlt";
        return new TopicPartition(deadLetterTopic, record.partition());
    }
}
