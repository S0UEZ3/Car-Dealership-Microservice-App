package org.example.dataAccess.repositories;

import org.example.dataAccess.models.TestDriveRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ITestDriveRequestRepository extends JpaRepository<TestDriveRequest, UUID> {
    List<TestDriveRequest> findByUserId(UUID userId);
}
