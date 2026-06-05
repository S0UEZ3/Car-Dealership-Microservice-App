package org.example.dataAccess.repositories;

import org.example.dataAccess.models.ComponentStock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ComponentStockRepository extends JpaRepository<ComponentStock, UUID> {
    Optional<ComponentStock> findByComponentId(UUID componentId);
    boolean existsByComponentId(UUID componentId);
}
