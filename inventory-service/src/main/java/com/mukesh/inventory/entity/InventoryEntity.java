package com.mukesh.inventory.entity;

import javax.annotation.processing.SupportedSourceVersion;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name="inventory")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryEntity {
    @Id
    private UUID productId;

    //private String productName;

    @Column(nullable= false)
    private Integer availableQuantity;

    @Column(nullable=false)
    private Integer reservedQuantity;

    @Version
    @Column(nullable=false)
    private Long version;

    @Column(nullable = false)
    private Instant updatedAt;
}
