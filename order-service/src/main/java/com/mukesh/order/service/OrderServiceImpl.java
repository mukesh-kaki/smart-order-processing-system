package com.mukesh.order.service;

import com.mukesh.commonoutbox.entity.AggregateType;
import com.mukesh.commonoutbox.idempotency.service.ProcessedEventService;
import com.mukesh.commonoutbox.service.OutboxPublisherService;
import com.mukesh.events.DomainEvent;
import com.mukesh.events.InventoryReleaseEvent;
import com.mukesh.events.InventoryReservedEvent;
import com.mukesh.events.OrderCreatedEvent;
import com.mukesh.events.PaymentCompletedEvent;
import com.mukesh.events.PaymentFailedEvent;
import com.mukesh.events.PaymentRequestedEvent;
import com.mukesh.order.dto.CreateOrderRequest;
import com.mukesh.order.dto.OrderResponse;
import com.mukesh.order.entity.OrderEntity;
import com.mukesh.order.entity.OrderStatus;
import com.mukesh.order.factory.OrderDomainEventFactory;
import com.mukesh.order.mapper.OrderMapper;
import com.mukesh.order.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderDomainEventFactory orderDomainEventFactory;
    private final OutboxPublisherService outboxPublisherService;
    private final ProcessedEventService processedEventService;

    @Override
    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request) {

        OrderEntity order = orderMapper.buildOrder(request);

        order.setStatus(OrderStatus.CREATED);

        OrderEntity savedOrder = orderRepository.save(order);

        OrderCreatedEvent event =
                orderDomainEventFactory.createOrderCreatedEvent(savedOrder);

        publishOrderEvent(savedOrder.getOrderId(), event);

        return orderMapper.toResponse(savedOrder);
    }

    @Override
    @Transactional
    public void handleInventoryReserved(InventoryReservedEvent event) {

        OrderEntity order = getOrder(event.orderId());

        order.setStatus(OrderStatus.INVENTORY_RESERVED);

        PaymentRequestedEvent paymentRequestedEvent =
                orderDomainEventFactory.createPaymentRequestedEvent(order);

        publishOrderEvent(order.getOrderId(), paymentRequestedEvent);

        processedEventService.markProcessed(
                event,
                getClass()
        );
    }

    @Override
    @Transactional
    public void handlePaymentCompleted(PaymentCompletedEvent event) {

        OrderEntity order = getOrder(event.orderId());

        order.setStatus(OrderStatus.COMPLETED);

        processedEventService.markProcessed(
                event,
                getClass()
        );
    }

    @Override
    @Transactional
    public void handlePaymentFailed(PaymentFailedEvent event) {

        OrderEntity order = getOrder(event.orderId());

        order.setStatus(OrderStatus.PAYMENT_FAILED);

        for (InventoryReleaseEvent releaseEvent :
                orderDomainEventFactory.createInventoryReleaseEvents(order)) {

            publishOrderEvent(order.getOrderId(), releaseEvent);
        }

        processedEventService.markProcessed(
                event,
                getClass()
        );
    }

    private OrderEntity getOrder(UUID orderId) {

        return orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Order not found : " + orderId
                        ));
    }

    private void publishOrderEvent(
            UUID aggregateId,
            DomainEvent event
    ) {

        outboxPublisherService.publish(
                aggregateId,
                AggregateType.ORDER,
                event
        );
    }
}
