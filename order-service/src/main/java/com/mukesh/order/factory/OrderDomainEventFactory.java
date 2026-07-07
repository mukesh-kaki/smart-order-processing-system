package com.mukesh.order.factory;

import com.mukesh.events.InventoryReleasedEvent;
import com.mukesh.events.OrderCreatedEvent;
import com.mukesh.events.OrderItem;
import com.mukesh.events.PaymentRequestedEvent;
import com.mukesh.order.entity.OrderEntity;
import com.mukesh.order.entity.OrderItemEntity;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Component
public class OrderDomainEventFactory {

    /**
     * Creates an OrderCreatedEvent after an order
     * has been successfully persisted.
     */
    public OrderCreatedEvent createOrderCreatedEvent(
            OrderEntity order
    ) {

        return new OrderCreatedEvent(
                UUID.randomUUID(),
                order.getOrderId(),
                order.getCustomerId(),
                mapItems(order.getItems()),
                order.getTotalAmount(),
                order.getCurrency(),
                Instant.now()
        );

    }

    /**
     * Creates PaymentRequestedEvent
     * after inventory has been reserved.
     */
    public PaymentRequestedEvent createPaymentRequestedEvent(
            OrderEntity order
    ) {

        return new PaymentRequestedEvent(
                UUID.randomUUID(),
                order.getOrderId(),
                order.getCustomerId(),
                order.getTotalAmount(),
                order.getCurrency(),
                Instant.now()
        );

    }

    /**
     * Compensation events.
     *
     * One InventoryReleaseEvent is created
     * for every reserved product.
     */
    public List<InventoryReleasedEvent> createInventoryReleaseEvents(
            OrderEntity order
    ) {

        return order.getItems()
                .stream()
                .map(item -> createInventoryReleaseEvent(order, item))
                .toList();

    }

    private InventoryReleasedEvent createInventoryReleaseEvent(
            OrderEntity order,
            OrderItemEntity item
    ) {

        return new InventoryReleasedEvent(
                UUID.randomUUID(),
                order.getOrderId(),
                order.getCustomerId(),
                item.getProductId(),
                item.getQuantity(),
                Instant.now()
        );

    }

    private List<OrderItem> mapItems(
            List<OrderItemEntity> items
    ) {

        return items.stream()
                .map(this::mapItem)
                .toList();

    }

    private OrderItem mapItem(
            OrderItemEntity item
    ) {

        return new OrderItem(
                item.getProductId(),
                null,
                item.getQuantity(),
                null
        );

    }

}