package org.example.dataAccess.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "orders")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class Order extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "client_id")
    private User client;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private User manager;
}
