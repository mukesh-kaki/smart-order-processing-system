package com.mukesh.inventory.service;

import com.mukesh.events.*;
import com.mukesh.inventory.entity.InventoryEntity;
import com.mukesh.inventory.mapper.InventoryMapper;
import com.mukesh.inventory.publisher.InventoryEventPublisher;
import com.mukesh.inventory.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final InventoryMapper inventoryMapper;
    private final InventoryEventPublisher inventoryEventPublisher;

    @Override
    @Transactional
    public void reserveInventory(OrderCreatedEvent event) {

        log.info(
                "Starting inventory reservation for Order {}",
                event.orderId()
        );

        for (OrderItem item : event.items()) {

            InventoryEntity inventory = inventoryRepository
                    .findByProductId(item.productId())
                    .orElseThrow(() ->
                            new IllegalArgumentException(
                                    "Inventory not found for Product "
                                            + item.productId()));

            if (inventory.getAvailableQuantity() < item.quantity()) {

                throw new IllegalStateException(
                        "Insufficient inventory for Product "
                                + item.productId()
                );
            }

            inventory.setAvailableQuantity(
                    inventory.getAvailableQuantity() - item.quantity());

            inventory.setReservedQuantity(
                    inventory.getReservedQuantity() + item.quantity());

            inventory.setUpdatedAt(Instant.now());

            inventoryRepository.save(inventory);

            log.info(
                    "Reserved {} units for Product {}",
                    item.quantity(),
                    item.productId()
            );
        }

        InventoryReservedEvent reservedEvent =
                inventoryMapper.toReservedEvent(event);

        inventoryEventPublisher.publishInventoryReserved(
                event.orderId(),
                reservedEvent
        );

        log.info(
                "Published InventoryReservedEvent for Order {}",
                event.orderId()
        );
    }

    @Override
    @Transactional
    public void releaseInventory(
            InventoryReleaseEvent event) {

        log.info(
                "Releasing inventory for Product {}",
                event.productId()
        );

        InventoryEntity inventory =
                inventoryRepository.findByProductId(
                                event.productId())
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Inventory not found for Product "
                                                + event.productId()));

        inventory.setReservedQuantity(
                inventory.getReservedQuantity()
                        - event.quantity());

        inventory.setAvailableQuantity(
                inventory.getAvailableQuantity()
                        + event.quantity());

        inventory.setUpdatedAt(Instant.now());

        inventoryRepository.save(inventory);

        log.info(
                "Released {} units for Product {}",
                event.quantity(),
                event.productId()
        );

        InventoryReleasedEvent releasedEvent =
                inventoryMapper.toReleasedEvent(event);

        inventoryEventPublisher.publishInventoryReleased(
                event.orderId(),
                releasedEvent
        );

        log.info(
                "Published InventoryReleasedEvent for Order {}",
                event.orderId()
        );
    }

}