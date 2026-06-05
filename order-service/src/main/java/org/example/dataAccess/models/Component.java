package org.example.dataAccess.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "components")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Component extends BaseEntity {
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ComponentType type;

    private String name;

    private String description;

    private double price;

    @ManyToMany
    @JoinTable(
            name = "component_compatible_models",
            joinColumns = @JoinColumn(name = "component_id"),
            inverseJoinColumns = @JoinColumn(name = "model_id")
    )
    private Set<CarModel> compatibleModels = new HashSet<>();
}
