package com.mukesh.inventory.repository;

import com.mukesh.inventory.entity.InventoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface InventoryRepository extends JpaRepository<InventoryEntity, UUID> {

    Optional<InventoryEntity> findByProductId(UUID productId);

}
