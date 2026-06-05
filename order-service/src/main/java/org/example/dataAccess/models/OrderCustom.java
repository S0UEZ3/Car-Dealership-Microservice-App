package org.example.dataAccess.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "orders_custom")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class OrderCustom extends Order {
    @Enumerated(EnumType.STRING)
    private OrderStatusCustom status;

    @ManyToOne
    @JoinColumn(name = "car_model_id")
    private CarModel carModel;

    @ElementCollection
    @CollectionTable(name = "order_custom_components",
            joinColumns = @JoinColumn(name = "order_id"))
    @MapKeyColumn(name = "component_type")
    @Column(name = "component_id")
    private Map<ComponentType, UUID> selectedComponentIds = new HashMap<>();
}
