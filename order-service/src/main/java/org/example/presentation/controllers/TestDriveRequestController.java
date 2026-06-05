package org.example.presentation.controllers;

import lombok.RequiredArgsConstructor;
import org.example.application.services.TestDriveRequestService;
import org.example.presentation.dto.objects.TestDriveRequestDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/test-drive-requests")
@RequiredArgsConstructor
public class TestDriveRequestController {
    private final TestDriveRequestService testDriveRequestService;

    @GetMapping
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public ResponseEntity<List<TestDriveRequestDto>> getAllRequests() {
        return ResponseEntity.ok(testDriveRequestService.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public ResponseEntity<TestDriveRequestDto> getRequestById(@PathVariable UUID id,
                                                              @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(testDriveRequestService.findById(id, jwt));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<TestDriveRequestDto> createRequest(
            @RequestParam UUID carId,
            @RequestParam LocalDateTime startTime,
            @AuthenticationPrincipal Jwt jwt) {
        UUID userId = UUID.fromString(jwt.getClaim("sub"));
        TestDriveRequestDto created = testDriveRequestService.createTestDriveRequest(userId, carId, startTime);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'MANAGER')")
    public ResponseEntity<Void> cancelRequest(@PathVariable UUID id, @AuthenticationPrincipal Jwt jwt) {
        UUID userId = UUID.fromString(jwt.getClaim("sub"));
        testDriveRequestService.cancelTestDriveRequest(id, jwt);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/complete")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Void> completeRequest(@PathVariable UUID id) {
        testDriveRequestService.completeTestDriveRequest(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteRequest(@PathVariable UUID id) {
        testDriveRequestService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}