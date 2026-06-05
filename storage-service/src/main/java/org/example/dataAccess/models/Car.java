package org.example.dataAccess.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@Table(name = "cars")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Car extends BaseEntity {
    @Column(name = "model_id", nullable = false)
    private UUID modelId;

    @Enumerated(EnumType.STRING)
    @Column(name = "body_type", length = 50)
    private CarBodyType bodyType;

    @Enumerated(EnumType.STRING)
    @Column(name = "fuel_type", length = 50)
    private CarFuelType fuelType;

    @Column(name = "engine_power")
    private double enginePower;

    @Column(name = "engine_volume")
    private double engineVolume;

    @Enumerated(EnumType.STRING)
    @Column(name = "transmission", length = 50)
    private CarTransmissionType transmission;

    @Enumerated(EnumType.STRING)
    @Column(name = "drive_type", length = 50)
    private CarDriveType driveType;

    @Column(length = 50)
    private String color;

    @Column(name = "in_stock")
    private boolean inStock;

    @Column(name = "total_cost")
    private Double totalCost;

    @Column(name = "available_for_test_drive")
    private boolean availableForTestDrive;

    @ElementCollection
    @CollectionTable(name = "car_configuration",
            joinColumns = @JoinColumn(name = "car_id"))
    @Column(name = "component_id")
    private Set<UUID> configurationComponentIds = new HashSet<>();
}
