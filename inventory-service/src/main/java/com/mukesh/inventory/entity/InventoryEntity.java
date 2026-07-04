package com.mukesh.inventory.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "inventory")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryEntity {

    @Id
    private UUID productId;

    //private String productName;

    @Column(nullable = false)
    private Integer availableQuantity;

    @Column(nullable = false)
    private Integer reservedQuantity;

    @Version
    @Column(nullable = false)
    private Long version;

    @Column(nullable = false)
    private Instant updatedAt;
}
