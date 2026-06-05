package org.example.dataAccess.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "orders_in_stock")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class OrderInStock extends Order {
    private UUID carId;

    @Enumerated(EnumType.STRING)
    private OrderStatusInStock status;
}
