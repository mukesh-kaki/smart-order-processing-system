package com.mukesh.order.consumer;

import com.mukesh.commonoutbox.idempotency.AbstractIdempotentConsumer;
import com.mukesh.commonoutbox.idempotency.service.ProcessedEventService;
import com.mukesh.events.InventoryReservedEvent;
import com.mukesh.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class InventoryReservedConsumer
        extends AbstractIdempotentConsumer<InventoryReservedEvent> {

    private final OrderService orderService;

    public InventoryReservedConsumer(
            OrderService orderService,
            ProcessedEventService processedEventService
    ) {
        super(processedEventService);
        this.orderService = orderService;
    }

    @KafkaListener(
            topics = "${app.kafka.topics.mappings.InventoryReservedEvent}"
    )
    public void consume(InventoryReservedEvent event) {

        log.info(
                "Received InventoryReservedEvent for Order {}",
                event.orderId()
        );

        process(event);
    }

    @Override
    protected void handle(InventoryReservedEvent event) {
        orderService.handleInventoryReserved(event);
    }
}