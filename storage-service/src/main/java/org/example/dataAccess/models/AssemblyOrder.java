package org.example.dataAccess.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "assembly_orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AssemblyOrder extends BaseEntity {
    @Column(name = "source_order_id", nullable = false)
    private UUID sourceOrderId;

    @Column(name = "source_order_type", nullable = false)
    private String sourceOrderType;

    @Column(name = "car_id")
    private UUID carId;

    @Column(name = "car_model_id")
    private UUID carModelId;

    @Column(name = "required_component_ids", columnDefinition = "TEXT")
    private String requiredComponentIds;

    @Column(name = "responsible_manager_id")
    private UUID responsibleManagerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AssemblyOrderStatus status;
}
