package com.mukesh.order.service;

import com.mukesh.events.InventoryReleasedEvent;
import com.mukesh.events.InventoryReservedEvent;
import com.mukesh.events.PaymentCompletedEvent;
import com.mukesh.events.PaymentFailedEvent;
import com.mukesh.order.dto.CreateOrderRequest;
import com.mukesh.order.dto.OrderResponse;

public interface OrderService {

    OrderResponse createOrder(CreateOrderRequest request);

    void handleInventoryReserved(InventoryReservedEvent event);

    void handlePaymentCompleted(PaymentCompletedEvent event);

    void handlePaymentFailed(PaymentFailedEvent event);

    void handleInventoryReleased(InventoryReleasedEvent event);

}