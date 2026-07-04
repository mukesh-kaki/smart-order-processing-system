package com.mukesh.inventory.mapper;

import com.mukesh.events.InventoryReservedEvent;
import com.mukesh.events.OrderCreatedEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Instant;

@Mapper(componentModel = "spring", imports = Instant.class)
public interface InventoryMapper {

    @Mapping(target = "eventId", expression = "java(java.util.UUID.randomUUID())")
    @Mapping(target = "reservedAt", expression = "java(java.time.Instant.now())")
    InventoryReservedEvent toReservedEvent(OrderCreatedEvent event);

}
