package org.example.presentation.controllers;

import lombok.RequiredArgsConstructor;
import org.example.application.services.AssemblyOrderService;
import org.example.dataAccess.models.AssemblyOrder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/assembly-orders")
@RequiredArgsConstructor
public class AssemblyOrderController {
    private final AssemblyOrderService assemblyOrderService;

    @GetMapping
    @PreAuthorize("hasRole('WAREHOUSE_ADMIN') or hasRole('ADMIN')")
    public ResponseEntity<List<AssemblyOrder>> getAll() {
        return ResponseEntity.ok(assemblyOrderService.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('WAREHOUSE_ADMIN') or hasRole('ADMIN')")
    public ResponseEntity<AssemblyOrder> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(assemblyOrderService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('WAREHOUSE_ADMIN') or hasRole('ADMIN')")
    public ResponseEntity<AssemblyOrder> create(@RequestBody AssemblyOrder order) {
        return new ResponseEntity<>(assemblyOrderService.create(order), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('WAREHOUSE_ADMIN') or hasRole('ADMIN')")
    public ResponseEntity<AssemblyOrder> update(@PathVariable UUID id, @RequestBody AssemblyOrder order) {
        order.setId(id);
        return ResponseEntity.ok(assemblyOrderService.update(order));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('WAREHOUSE_ADMIN') or hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        assemblyOrderService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
