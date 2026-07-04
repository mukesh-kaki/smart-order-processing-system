package com.mukesh.inventory.repository;

import com.mukesh.inventory.entity.InventoryEntity;

import java.util.UUID;

public interface InventoryRepository extends JpaRepository<InventoryEntity, UUID>{
}
