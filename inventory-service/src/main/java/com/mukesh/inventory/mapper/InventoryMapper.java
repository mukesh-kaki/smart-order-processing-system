package com.mukesh.inventory.mapper;

import com.mukesh.events.InventoryReservedEvent;
import com.mukesh.events.OrderCreatedEvent;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class InventoryMapper {

    @Mapping(
            target = "eventId",
            expression = "java(java.util.UUID.randomUUID())"
    )
    public InventoryReservedEvent toReservedEvent(OrderCreatedEvent event){
        return  new InventoryReservedEvent(event.orderId(), event.customerId(), event.items(), Instant.now());
    }
}
