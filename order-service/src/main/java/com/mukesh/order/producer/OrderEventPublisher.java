package com.mukesh.order.producer;

import com.mukesh.events.OrderCreatedEvent;

public interface OrderEventPublisher {

    void publishOrderCreated(OrderCreatedEvent event);
}
