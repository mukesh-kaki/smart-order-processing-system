package com.mukesh.order.producer;

import com.mukesh.order.events.OrderCreatedEvent;

public interface OrderEventPublisher {

    void publishOrderCreated(OrderCreatedEvent event);
}
