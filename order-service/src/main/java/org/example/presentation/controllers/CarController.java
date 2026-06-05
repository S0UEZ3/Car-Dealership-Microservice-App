package org.example.presentation.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.client.CarStockGrpcClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/cars")
@RequiredArgsConstructor
@Slf4j
public class CarController {

    private final CarStockGrpcClient grpcClient;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getAllCars() {
        try {
            var cars = grpcClient.getAllCars();
            return ResponseEntity.ok(cars);
        } catch (Exception e) {
            log.error("Failed to get cars from StorageService", e);
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body("{\"error\": \"StorageService unavailable\"}");
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getCarById(@PathVariable UUID id) {
        try {
            var car = grpcClient.getCarById(id);
            return ResponseEntity.ok(car);
        } catch (Exception e) {
            log.error("Failed to get car {} from StorageService", id, e);
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body("{\"error\": \"StorageService unavailable\"}");
        }
    }
}
