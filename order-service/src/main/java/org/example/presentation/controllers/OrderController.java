package org.example.presentation.controllers;

import lombok.RequiredArgsConstructor;
import org.example.application.services.OrderService;
import org.example.dataAccess.models.ComponentType;
import org.example.dataAccess.models.OrderStatusCustom;
import org.example.dataAccess.models.OrderStatusInStock;
import org.example.presentation.dto.objects.OrderCustomDto;
import org.example.presentation.dto.objects.OrderDto;
import org.example.presentation.dto.objects.OrderInStockDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable UUID id,
                                                 @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(orderService.findById(id, jwt));
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<OrderDto>> getOrders(Authentication authentication) {
        return ResponseEntity.ok(orderService.getOrdersForCurrentUser(authentication));
    }

    @GetMapping("/in-stock")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN', 'STOCK_ADMIN')")
    public ResponseEntity<List<OrderDto>> getAllInStockOrders() {
        return ResponseEntity.ok(orderService.findAllInStockOrders());
    }

    @GetMapping("/custom")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN', 'STOCK_ADMIN')")
    public ResponseEntity<List<OrderDto>> getAllCustomOrders() {
        return ResponseEntity.ok(orderService.findAllCustomOrders());
    }

    @GetMapping("/client/{clientId}")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public ResponseEntity<List<OrderDto>> getOrdersByClient(@PathVariable UUID clientId) {
        return ResponseEntity.ok(orderService.findByClientId(clientId));
    }

    @PostMapping("/in-stock")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<OrderDto> createInStockOrder(
            @RequestParam UUID carId,
            @AuthenticationPrincipal Jwt jwt) { // get client_id from jwt now
        UUID clientId = UUID.fromString(jwt.getClaim("sub"));
        OrderDto created = orderService.createOrderInStock(clientId, carId);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PostMapping("/custom")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<OrderDto> createCustomOrder(
            @RequestParam UUID carModelId,
            @RequestBody Map<ComponentType, UUID> components,
            @AuthenticationPrincipal Jwt jwt) {
        UUID clientId = UUID.fromString(jwt.getClaim("sub"));
        OrderDto created = orderService.createOrderCustom(clientId, carModelId, components);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}/in-stock-status")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public ResponseEntity<OrderDto> updateInStockStatus(
            @PathVariable UUID id,
            @RequestParam OrderStatusInStock status) {
        OrderDto updated = orderService.updateInStockOrderStatus(id, status);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}/custom-status")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public ResponseEntity<OrderDto> updateCustomStatus(
            @PathVariable UUID id,
            @RequestParam OrderStatusCustom status) {
        OrderDto updated = orderService.updateCustomOrderStatus(id, status);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}/assign-manager")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public ResponseEntity<Void> assignManager(
            @PathVariable UUID id,
            @RequestParam UUID managerId) {
        orderService.assignManager(id, managerId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteOrder(@PathVariable UUID id) {
        orderService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
