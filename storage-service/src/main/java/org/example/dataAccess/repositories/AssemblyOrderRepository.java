package org.example.dataAccess.repositories;

import org.example.dataAccess.models.AssemblyOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AssemblyOrderRepository extends JpaRepository<AssemblyOrder, UUID> {
}
