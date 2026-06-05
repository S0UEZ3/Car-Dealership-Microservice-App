package org.example.dataAccess.repositories;

import org.example.dataAccess.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IOrderRepository extends JpaRepository<Order, UUID> {
}
