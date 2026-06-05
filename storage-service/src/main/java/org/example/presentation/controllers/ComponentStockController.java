package org.example.presentation.controllers;

import lombok.RequiredArgsConstructor;
import org.example.application.services.ComponentStockService;
import org.example.dataAccess.models.ComponentStock;
import org.example.presentation.dto.objects.ReservationRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/stock/parts")
@RequiredArgsConstructor
public class ComponentStockController {
    private final ComponentStockService componentStockService;

    @GetMapping
    @PreAuthorize("hasRole('WAREHOUSE_ADMIN') or hasRole('ADMIN')")
    public ResponseEntity<List<ComponentStock>> getAllPartsStock() {
        return ResponseEntity.ok(componentStockService.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('WAREHOUSE_ADMIN') or hasRole('ADMIN')")
    public ResponseEntity<ComponentStock> getPartStockById(@PathVariable UUID id) {
        return ResponseEntity.ok(componentStockService.findById(id));
    }

    @GetMapping("/by-component/{componentId}")
    @PreAuthorize("hasRole('WAREHOUSE_ADMIN') or hasRole('ADMIN')")
    public ResponseEntity<ComponentStock> getPartStockByComponentId(@PathVariable UUID componentId) {
        return ResponseEntity.ok(componentStockService.findByComponentId(componentId));
    }

    @GetMapping("/check")
    @PreAuthorize("hasRole('WAREHOUSE_ADMIN') or hasRole('ADMIN')")
    public ResponseEntity<Boolean> isPartAvailable(
            @RequestParam UUID componentId,
            @RequestParam Integer quantity) {
        return ResponseEntity.ok(componentStockService.isPartAvailable(componentId, quantity));
    }

    @PostMapping
    @PreAuthorize("hasRole('WAREHOUSE_ADMIN') or hasRole('ADMIN')")
    public ResponseEntity<ComponentStock> addPart(@RequestBody ComponentStock componentStock) {
        ComponentStock created = componentStockService.addPart(componentStock);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PostMapping("/{id}/add-quantity")
    @PreAuthorize("hasRole('WAREHOUSE_ADMIN') or hasRole('ADMIN')")
    public ResponseEntity<ComponentStock> addQuantity(
            @PathVariable UUID id,
            @RequestParam Integer quantity) {
        return ResponseEntity.ok(componentStockService.addQuantity(id, quantity));
    }

    @PostMapping("/reserve")
    @PreAuthorize("hasRole('WAREHOUSE_ADMIN') or hasRole('ADMIN')")
    public ResponseEntity<Void> reserveParts(@RequestBody List<ReservationRequest> requests) {
        componentStockService.reserveParts(requests);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/release")
    @PreAuthorize("hasRole('WAREHOUSE_ADMIN') or hasRole('ADMIN')")
    public ResponseEntity<Void> releaseParts(@RequestBody List<ReservationRequest> requests) {
        componentStockService.releaseParts(requests);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/deduct")
    @PreAuthorize("hasRole('WAREHOUSE_ADMIN') or hasRole('ADMIN')")
    public ResponseEntity<Void> deductParts(@RequestBody List<ReservationRequest> requests) {
        componentStockService.deductParts(requests);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('WAREHOUSE_ADMIN') or hasRole('ADMIN')")
    public ResponseEntity<ComponentStock> updatePartStock(@PathVariable UUID id, @RequestBody ComponentStock componentStock) {
        componentStock.setId(id);
        return ResponseEntity.ok(componentStockService.update(componentStock));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('WAREHOUSE_ADMIN') or hasRole('ADMIN')")
    public ResponseEntity<Void> deletePartStock(@PathVariable UUID id) {
        componentStockService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
