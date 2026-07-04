package com.mukesh.inventory.service;

import com.mukesh.events.InventoryReleaseEvent;
import com.mukesh.events.InventoryReservedEvent;
import com.mukesh.events.OrderCreatedEvent;
import com.mukesh.events.OrderItem;
import com.mukesh.inventory.entity.InventoryEntity;
import com.mukesh.commonoutbox.entity.OutboxEntity;
import com.mukesh.inventory.mapper.InventoryMapper;
import com.mukesh.inventory.repository.InventoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import com.mukesh.commonoutbox.entity.AggregateType;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final OutboxFactory outboxFactory;
    private final OutboxService outboxService;
    private final InventoryMapper inventoryMapper;

    @Transactional
    @Override
    public void reserveInventory(OrderCreatedEvent event) {

        log.info("Reserving inventory for Order {}", event.orderId());

        for(OrderItem item:event.items()){
            InventoryEntity inventory= inventoryRepository.findById(item.productId())
                    .orElseThrow(()-> new RuntimeException("Inventory not found for product " + item.productId()));

            if(inventory.getAvailableQuantity() < item.quantity()){
                throw new RuntimeException("Insufficient inventory for product "+ item.productId());
            }

            inventory.setAvailableQuantity( inventory.getAvailableQuantity() - item.quantity());
            inventory.setReservedQuantity(inventory.getReservedQuantity() + item.quantity());

            inventory.setUpdatedAt(Instant.now());
            inventoryRepository.save(inventory);
        }

        InventoryReservedEvent reservedEvent=inventoryMapper.toReservedEvent(event);

        OutboxEntity outbox= outboxFactory.create(String.valueOf(event.orderId()),
                AggregateType.INVENTORY.name(),
                reservedEvent);
        outboxService.save(outbox);

    }

    @Transactional
    @Override
    public void releaseInventory(InventoryReleaseEvent event){
        InventoryEntity inventory =
                inventoryRepository.findByProductId(event.productId())
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Inventory not found : "
                                                + event.productId()));

        inventory.setReservedQuantity(
                inventory.getReservedQuantity() - event.quantity());

        inventory.setAvailableQuantity(
                inventory.getAvailableQuantity() + event.quantity());

        inventoryRepository.save(inventory);

        log.info(
                "Released {} units of product {}",
                event.quantity(),
                event.productId()
        );
    }

}