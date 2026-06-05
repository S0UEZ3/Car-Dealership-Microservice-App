package org.example.presentation.controllers;

import lombok.RequiredArgsConstructor;
import org.example.application.services.CarStockService;
import org.example.dataAccess.models.Car;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/stock/cars")
@RequiredArgsConstructor
public class CarStockController {
    private final CarStockService carStockService;

    @GetMapping
    @PreAuthorize("hasRole('WAREHOUSE_ADMIN') or hasRole('ADMIN')")
    public ResponseEntity<List<Car>> getAllCars() {
        return ResponseEntity.ok(carStockService.findAll());
    }

    @GetMapping("/in-stock")
    @PreAuthorize("hasRole('WAREHOUSE_ADMIN') or hasRole('ADMIN')")
    public ResponseEntity<List<Car>> getCarsInStock() {
        return ResponseEntity.ok(carStockService.findAllInStock());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('WAREHOUSE_ADMIN') or hasRole('ADMIN')")
    public ResponseEntity<Car> getCarById(@PathVariable UUID id) {
        return ResponseEntity.ok(carStockService.findById(id));
    }

    @GetMapping("/{id}/in-stock")
    @PreAuthorize("hasRole('WAREHOUSE_ADMIN') or hasRole('ADMIN')")
    public ResponseEntity<Boolean> isCarInStock(@PathVariable UUID id) {
        return ResponseEntity.ok(carStockService.isCarInStock(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('WAREHOUSE_ADMIN') or hasRole('ADMIN')")
    public ResponseEntity<Car> addCar(@RequestBody Car car) {
        Car created = carStockService.addCar(car);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PostMapping("/{id}/reserve")
    @PreAuthorize("hasRole('WAREHOUSE_ADMIN') or hasRole('ADMIN')")
    public ResponseEntity<Void> reserveCar(@PathVariable UUID id) {
        carStockService.reserveCar(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/release")
    @PreAuthorize("hasRole('WAREHOUSE_ADMIN') or hasRole('ADMIN')")
    public ResponseEntity<Void> releaseCar(@PathVariable UUID id) {
        carStockService.releaseCar(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('WAREHOUSE_ADMIN') or hasRole('ADMIN')")
    public ResponseEntity<Car> updateCar(@PathVariable UUID id, @RequestBody Car car) {
        car.setId(id);
        return ResponseEntity.ok(carStockService.update(car));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('WAREHOUSE_ADMIN') or hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCar(@PathVariable UUID id) {
        carStockService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/test-drive-availability")
    @PreAuthorize("hasRole('WAREHOUSE_ADMIN') or hasRole('ADMIN')")
    public ResponseEntity<Void> setTestDriveAvailability(
            @PathVariable UUID id,
            @RequestParam boolean available) {
        carStockService.setTestDriveAvailability(id, available);
        return ResponseEntity.ok().build();
    }
}
