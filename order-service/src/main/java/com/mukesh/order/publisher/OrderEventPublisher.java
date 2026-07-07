package com.mukesh.order.publisher;

import com.mukesh.events.InventoryReleasedEvent;
import com.mukesh.events.OrderCreatedEvent;
import com.mukesh.events.PaymentRequestedEvent;

import java.util.List;
import java.util.UUID;

public interface OrderEventPublisher {

    void publishOrderCreated(
            UUID orderId,
            OrderCreatedEvent event
    );

    void publishPaymentRequested(
            UUID orderId,
            PaymentRequestedEvent event
    );

    void publishInventoryRelease(
            UUID orderId,
            List<InventoryReleasedEvent> events
    );

}