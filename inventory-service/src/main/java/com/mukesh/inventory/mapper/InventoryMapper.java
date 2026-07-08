package com.mukesh.inventory.mapper;

import com.mukesh.events.InventoryReleaseEvent;
import com.mukesh.events.InventoryReleasedEvent;
import com.mukesh.events.InventoryReservedEvent;
import com.mukesh.events.OrderCreatedEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Instant;
import java.util.UUID;

@Mapper(
        componentModel = "spring",
        imports = {UUID.class, Instant.class}
)
public interface InventoryMapper {

    @Mapping(target = "eventId",
            expression = "java(UUID.randomUUID())")
    @Mapping(target = "reservedAt",
            expression = "java(Instant.now())")
    InventoryReservedEvent toReservedEvent(OrderCreatedEvent event);

    @Mapping(target = "eventId", expression = "java(UUID.randomUUID())")
    @Mapping(target = "releasedAt", expression = "java(Instant.now())")
    InventoryReleasedEvent toReleasedEvent(
            InventoryReleaseEvent event
    );

}