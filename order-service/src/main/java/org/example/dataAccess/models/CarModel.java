package org.example.dataAccess.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "car_models")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CarModel extends BaseEntity {
    private String brand;
    private String model;
    private double basePrice;
}
