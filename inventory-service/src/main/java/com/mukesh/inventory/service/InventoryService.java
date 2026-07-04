package com.mukesh.inventory.service;

import com.mukesh.events.InventoryReleaseEvent;
import com.mukesh.events.OrderCreatedEvent;

public interface InventoryService {

    void reserveInventory(OrderCreatedEvent event);

    void releaseInventory(InventoryReleaseEvent event);

}