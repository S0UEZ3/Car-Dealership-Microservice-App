package org.example.dataAccess.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "parts_stock")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ComponentStock extends BaseEntity {
    @Column(name = "component_id", nullable = false)
    private UUID componentId;

    @Column(name = "quantity", nullable = false)
    private Integer quantity = 0;

    @Column(name = "reserved_quantity")
    private Integer reservedQuantity = 0;
}
