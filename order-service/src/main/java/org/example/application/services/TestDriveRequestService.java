package org.example.application.services;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.example.application.exceptions.AccessDeniedException;
import org.example.application.exceptions.DomainValidationException;
import org.example.application.exceptions.EntityNotFoundException;
import org.example.dataAccess.models.*;
import org.example.dataAccess.repositories.ITestDriveRequestRepository;
import org.example.dataAccess.repositories.IUserRepository;
import org.example.presentation.dto.mapper.TestDriveRequestMapper;
import org.example.presentation.dto.mapper.UserMapper;
import org.example.presentation.dto.objects.TestDriveRequestDto;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TestDriveRequestService {
    private final IUserRepository userRepository;
    private final ITestDriveRequestRepository testDriveRequestRepository;
    private final TestDriveRequestMapper testDriveRequestMapper;
    private final UserMapper userMapper;

    @Transactional
    public TestDriveRequestDto createTestDriveRequest(UUID userId, UUID carId, LocalDateTime testDriveStartDateTime) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        // Проверка доступности машины для тест-драйва теперь должна идти через StorageService

        TestDriveRequest testDriveRequest = new TestDriveRequest();
        testDriveRequest.setUser(user);
        testDriveRequest.setCarId(carId);
        testDriveRequest.setTestDriveStartDateTime(testDriveStartDateTime);
        testDriveRequest.setTestDriveStatus(TestDriveStatus.SCHEDULED);

        TestDriveRequest savedRequest = testDriveRequestRepository.save(testDriveRequest);
        return testDriveRequestMapper.toDto(savedRequest);
    }

    public List<TestDriveRequestDto> getRequestsForCurrentUser(Jwt jwt) {
        List<String> roles = extractClientRoles(jwt);
        String userId = jwt.getClaim("sub");

        if (roles.contains("MANAGER") || roles.contains("ADMIN")) {
            return findAll();
        }

        return testDriveRequestMapper.toDtoList(
                testDriveRequestRepository.findByUserId(UUID.fromString(userId))
        );
    }

    public List<TestDriveRequestDto> findAll() {
        return testDriveRequestMapper.toDtoList(testDriveRequestRepository.findAll().stream()
                .sorted((r1, r2) -> r1.getTestDriveStartDateTime().compareTo(r2.getTestDriveStartDateTime()))
                .collect(Collectors.toList()));
    }

    public TestDriveRequestDto findById(UUID testDriveRequestId, Jwt jwt) {
        TestDriveRequest request = testDriveRequestRepository.findById(testDriveRequestId)
                .orElseThrow(() -> new EntityNotFoundException("Test drive request not found"));

        List<String> roles = extractClientRoles(jwt);
        String userId = jwt.getClaim("sub");

        boolean isAdminOrManager = roles.contains("ADMIN") || roles.contains("MANAGER");

        if (!isAdminOrManager && !request.getUser().getId().equals(UUID.fromString(userId))) {
            throw new AccessDeniedException("Access denied to this test drive request");
        }

        return testDriveRequestMapper.toDto(request);
    }

    // Временно убираем этот метод - он будет заменен на запрос к StorageService через REST или события
    // public List<CarDto> findAllTestDriveCars() { ... }

    public void deleteById(UUID requestId) {
        if (!testDriveRequestRepository.existsById(requestId)) {
            throw new EntityNotFoundException("Test drive request not found: " + requestId);
        }
        testDriveRequestRepository.deleteById(requestId);
    }

    public TestDriveRequestDto update(TestDriveRequestDto requestDto) {
        TestDriveRequest existingRequest = testDriveRequestRepository.findById(requestDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Test drive request not found: " + requestDto.getId()));
        testDriveRequestMapper.updateEntityFromDto(requestDto, existingRequest);
        TestDriveRequest updatedRequest = testDriveRequestRepository.save(existingRequest);
        return testDriveRequestMapper.toDto(updatedRequest);
    }

    @Transactional
    public void cancelTestDriveRequest(UUID requestId, Jwt jwt) {
        TestDriveRequest request = testDriveRequestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("Test drive request not found: " + requestId));

        List<String> roles = extractClientRoles(jwt);
        String userId = jwt.getClaim("sub");

        if (roles.contains("ADMIN") || roles.contains("MANAGER")) {
            request.setTestDriveStatus(TestDriveStatus.CANCELLED);
            testDriveRequestRepository.save(request);
            return;
        }

        if (!request.getUser().getId().equals(UUID.fromString(userId))) {
            throw new AccessDeniedException("You can only cancel your own test drive requests");
        }

        request.setTestDriveStatus(TestDriveStatus.CANCELLED);
        testDriveRequestRepository.save(request);
    }

    public void completeTestDriveRequest(UUID requestId) {
        TestDriveRequest request = testDriveRequestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("Test drive request not found: " + requestId));

        if (request.getTestDriveStatus() != TestDriveStatus.SCHEDULED) {
            throw new DomainValidationException("Only scheduled test drives can be completed");
        }

        request.setTestDriveStatus(TestDriveStatus.COMPLETED);
        testDriveRequestRepository.save(request);
    }

    /*private boolean isCarAvailableForTestDrive(Car car) {
        return car.isInStock() && car.isAvailableForTestDrive();
    }

    private void updateTestDriveFields(TestDriveRequest existing, TestDriveRequest updated) {
        if (updated.getUser() != null) {
            existing.setUser(updated.getUser());
        }
        if (updated.getCar() != null) {
            existing.setCar(updated.getCar());
        }
        if (updated.getTestDriveStartDateTime() != null) {
            existing.setTestDriveStartDateTime(updated.getTestDriveStartDateTime());
        }
        if (updated.getTestDriveStatus() != null) {
            existing.setTestDriveStatus(updated.getTestDriveStatus());
        }
    }
*/
    private List<String> extractClientRoles(Jwt jwt) {
        Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
        if (resourceAccess == null) return List.of();

        Map<String, Object> clientResource = (Map<String, Object>) resourceAccess.get("my-car-api");
        if (clientResource == null) return List.of();

        List<String> roles = (List<String>) clientResource.get("roles");
        return roles != null ? roles : List.of();
    }
}
