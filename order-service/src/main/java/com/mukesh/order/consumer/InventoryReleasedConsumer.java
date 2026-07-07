package com.mukesh.order.consumer;

import com.mukesh.commonoutbox.idempotency.AbstractIdempotentConsumer;
import com.mukesh.commonoutbox.idempotency.service.ProcessedEventService;
import com.mukesh.events.InventoryReleasedEvent;
import com.mukesh.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class InventoryReleasedConsumer extends AbstractIdempotentConsumer<InventoryReleasedEvent> {

    private final OrderService orderService;

    public InventoryReleasedConsumer(
            OrderService orderService,
            ProcessedEventService processedEventService) {

        super(processedEventService);
        this.orderService = orderService;
    }

    @KafkaListener(
            topics = "${app.kafka.topics.mappings.InventoryReleasedEvent}"
    )
    public void consume(
            InventoryReleasedEvent event) {

        log.info(
                "Received InventoryReleasedEvent for Order {}",
                event.orderId()
        );

        process(event);
    }

    @Override
    protected void handle(
            InventoryReleasedEvent event) {

        orderService.handleInventoryReleased(event);
    }
}