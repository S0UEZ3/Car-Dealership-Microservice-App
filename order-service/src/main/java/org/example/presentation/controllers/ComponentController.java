package org.example.presentation.controllers;

import lombok.RequiredArgsConstructor;
import org.example.application.exceptions.ResponseStatusNotFoundException;
import org.example.application.services.ComponentService;
import org.example.presentation.dto.objects.ComponentDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/components")
@RequiredArgsConstructor
public class ComponentController {

    private final ComponentService componentService;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ComponentDto>> getAllComponents() {
        return ResponseEntity.ok(componentService.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ComponentDto> getComponentById(@PathVariable UUID id) {
        return ResponseEntity.ok(componentService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('STOCK_ADMIN') or hasRole('ADMIN')")
    public ResponseEntity<ComponentDto> createComponent(@Valid @RequestBody ComponentDto componentDto) {
        ComponentDto created = componentService.save(componentDto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('STOCK_ADMIN') or hasRole('ADMIN')")
    public ResponseEntity<ComponentDto> updateComponent(
            @PathVariable UUID id,
            @Valid @RequestBody ComponentDto componentDto) {
        componentDto.setId(id);
        ComponentDto updated = componentService.update(componentDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('STOCK_ADMIN') or hasRole('ADMIN')")
    public ResponseEntity<Void> deleteComponent(@PathVariable UUID id) {
        componentService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
