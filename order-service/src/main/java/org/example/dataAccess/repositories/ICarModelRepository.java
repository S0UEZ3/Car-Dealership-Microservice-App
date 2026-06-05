package org.example.dataAccess.repositories;

import org.example.dataAccess.models.CarModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ICarModelRepository extends JpaRepository<CarModel, UUID> {
}