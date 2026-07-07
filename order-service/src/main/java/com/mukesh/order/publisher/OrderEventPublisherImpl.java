package com.mukesh.order.publisher;


import com.mukesh.commonoutbox.entity.AggregateType;
import com.mukesh.commonoutbox.service.OutboxPublisherService;
import com.mukesh.events.InventoryReleasedEvent;
import com.mukesh.events.OrderCreatedEvent;
import com.mukesh.events.PaymentRequestedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderEventPublisherImpl implements OrderEventPublisher{

    private final OutboxPublisherService outboxPublisherService;

    @Override
    public void publishOrderCreated(
            UUID orderId,
            OrderCreatedEvent event) {

        outboxPublisherService.publish(
                orderId,
                AggregateType.ORDER,
                event
        );

        log.info(
                "Published OrderCreatedEvent for Order {}",
                orderId
        );
    }

    @Override
    public void publishPaymentRequested(
            UUID orderId,
            PaymentRequestedEvent event) {

        outboxPublisherService.publish(
                orderId,
                AggregateType.ORDER,
                event
        );

        log.info(
                "Published PaymentRequestedEvent for Order {}",
                orderId
        );
    }

    @Override
    public void publishInventoryRelease(
            UUID orderId,
            List<InventoryReleasedEvent> events) {

        for (InventoryReleasedEvent event : events) {

            outboxPublisherService.publish(
                    orderId,
                    AggregateType.ORDER,
                    event
            );
        }

        log.info(
                "Published {} InventoryReleaseEvents for Order {}",
                events.size(),
                orderId
        );
    }
}
