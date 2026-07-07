package com.mukesh.order.producer;

import com.mukesh.events.DomainEvent;

import java.util.concurrent.ExecutionException;

public interface KafkaEventPublisher {
    void publish(String topic, String key, DomainEvent event) throws ExecutionException, InterruptedException;;
}
