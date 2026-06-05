package org.example.dataAccess.repositories;

import org.example.dataAccess.models.Component;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IComponentRepository extends JpaRepository<Component, UUID> {
}
