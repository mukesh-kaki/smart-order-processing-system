package com.mukesh.order.service;

import com.mukesh.events.InventoryReleasedEvent;
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
import com.mukesh.order.publisher.OrderEventPublisher;
import com.mukesh.order.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderDomainEventFactory orderDomainEventFactory;
    private final OrderEventPublisher orderEventPublisher;

    @Override
    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request) {

        log.info("Creating order for customer {}", request.customerId());

        OrderEntity order = orderMapper.buildOrder(request);

        // NEW -> CREATED
        order.transitionTo(OrderStatus.CREATED);

        OrderEntity savedOrder = orderRepository.save(order);

        log.info(
                "Order {} created successfully",
                savedOrder.getOrderId()
        );

        OrderCreatedEvent orderCreatedEvent =
                orderDomainEventFactory.createOrderCreatedEvent(savedOrder);

        orderEventPublisher.publishOrderCreated(
                savedOrder.getOrderId(),
                orderCreatedEvent
        );

        log.info(
                "Published OrderCreatedEvent for Order {}",
                savedOrder.getOrderId()
        );

        // CREATED -> INVENTORY_PENDING
        savedOrder.transitionTo(OrderStatus.INVENTORY_PENDING);

        orderRepository.save(savedOrder);

        log.info(
                "Order {} moved to INVENTORY_PENDING",
                savedOrder.getOrderId()
        );

        return orderMapper.toResponse(savedOrder);
    }

    @Override
    @Transactional
    public void handleInventoryReserved(
            InventoryReservedEvent event) {

        log.info(
                "Received InventoryReservedEvent for Order {}",
                event.orderId()
        );

        OrderEntity order = getOrder(event.orderId());

        // INVENTORY_PENDING -> INVENTORY_RESERVED
        order.transitionTo(OrderStatus.INVENTORY_RESERVED);

        orderRepository.save(order);

        log.info(
                "Inventory reserved for Order {}",
                order.getOrderId()
        );

        PaymentRequestedEvent paymentRequestedEvent =
                orderDomainEventFactory.createPaymentRequestedEvent(order);

        orderEventPublisher.publishPaymentRequested(
                order.getOrderId(),
                paymentRequestedEvent
        );

        log.info(
                "Published PaymentRequestedEvent for Order {}",
                order.getOrderId()
        );

        // INVENTORY_RESERVED -> PAYMENT_PENDING
        order.transitionTo(OrderStatus.PAYMENT_PENDING);

        orderRepository.save(order);

        log.info(
                "Order {} moved to PAYMENT_PENDING",
                order.getOrderId()
        );
    }

    @Override
    @Transactional
    public void handlePaymentCompleted(
            PaymentCompletedEvent event) {

        log.info(
                "Received PaymentCompletedEvent for Order {}",
                event.orderId()
        );

        OrderEntity order = getOrder(event.orderId());

        // PAYMENT_PENDING -> PAYMENT_COMPLETED
        order.transitionTo(OrderStatus.PAYMENT_COMPLETED);

        orderRepository.save(order);

        log.info(
                "Payment completed for Order {}",
                order.getOrderId()
        );

        // PAYMENT_COMPLETED -> COMPLETED
        order.transitionTo(OrderStatus.COMPLETED);

        orderRepository.save(order);

        log.info(
                "Order {} completed successfully",
                order.getOrderId()
        );
    }

    @Override
    @Transactional
    public void handlePaymentFailed(
            PaymentFailedEvent event) {

        log.warn(
                "Payment failed for Order {}",
                event.orderId()
        );

        OrderEntity order = getOrder(event.orderId());

        // PAYMENT_PENDING -> PAYMENT_FAILED
        order.transitionTo(OrderStatus.PAYMENT_FAILED);

        orderRepository.save(order);

        List<InventoryReleasedEvent> releaseEvents =
                orderDomainEventFactory.createInventoryReleaseEvents(order);

        orderEventPublisher.publishInventoryRelease(
                order.getOrderId(),
                releaseEvents
        );

        log.info(
                "Published {} InventoryReleaseEvents for Order {}",
                releaseEvents.size(),
                order.getOrderId()
        );

        /*
         * Order is NOT cancelled here.
         *
         * Wait until InventoryService confirms
         * inventory has been released.
         *
         * PAYMENT_FAILED
         *          │
         *          ▼
         * InventoryReleaseEvent
         *          │
         *          ▼
         * InventoryReleasedEvent
         *          │
         *          ▼
         * CANCELLED
         */
    }

    @Override
    @Transactional
    public void handleInventoryReleased(InventoryReleasedEvent event) {

        log.info(
                "Received InventoryReleasedEvent for Order {}",
                event.orderId()
        );

        OrderEntity order = getOrder(event.orderId());

        /*
         * PAYMENT_FAILED
         *        ↓
         * CANCELLED
         */
        order.transitionTo(OrderStatus.CANCELLED);

        orderRepository.save(order);

        log.info(
                "Order {} cancelled successfully",
                order.getOrderId()
        );
    }

    private OrderEntity getOrder(UUID orderId) {

        return orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Order not found : " + orderId
                        ));
    }
}