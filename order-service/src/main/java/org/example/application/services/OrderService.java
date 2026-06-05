package org.example.application.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.application.exceptions.AccessDeniedException;
import org.example.application.exceptions.DomainValidationException;
import org.example.application.exceptions.EntityNotFoundException;
import org.example.dataAccess.models.*;
import org.example.dataAccess.repositories.*;
import org.example.events.OrderEventPublisher;
import org.example.presentation.dto.mapper.*;
import org.example.presentation.dto.objects.OrderDto;
import org.example.presentation.dto.objects.OrderInStockDto;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.example.security.JwtAuthConverter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class OrderService {
    private final IOrderRepository orderRepository;
    private final IUserRepository userRepository;
    private final IComponentRepository componentRepository;
    private final ICarModelRepository carModelRepository;
    private final Configurator configurator;
    private final OrderMapper orderMapper;
    private final UserMapper userMapper;
    private final CarModelMapper carModelMapper;
    private final ComponentMapper componentMapper;
    private final OrderEventPublisher eventPublisher;

    public OrderDto findById(UUID orderId, Jwt jwt) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found: " + orderId));

        List<String> roles = extractClientRoles(jwt);
        String userId = jwt.getClaim("sub");

        boolean isAdminOrManager = roles.contains("ADMIN") || roles.contains("MANAGER");

        if (!isAdminOrManager && !order.getClient().getId().equals(UUID.fromString(userId))) {
            throw new AccessDeniedException("Access denied to this order");
        }

        return orderMapper.toDto(order);
    }

    public List<OrderDto> getOrdersForCurrentUser(Authentication authentication) {
        Jwt jwt = (Jwt) authentication.getPrincipal();
        List<String> roles = extractClientRoles(jwt);
        String userId = jwt.getClaim("sub");

        if (roles.contains("MANAGER") || roles.contains("ADMIN")) {
            List<OrderDto> inStockOrders = findAllInStockOrders();
            List<OrderDto> customOrders = findAllCustomOrders();
            return Stream.concat(inStockOrders.stream(), customOrders.stream())
                    .collect(Collectors.toList());
        }

        return findByClientId(UUID.fromString(userId));
    }

    @Transactional
    public OrderDto createOrderInStock(UUID clientId, UUID carId) {
        User client = userRepository.findById(clientId)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + clientId));

        User manager = assignRandomManager();

        OrderInStock order = new OrderInStock();
        order.setId(UUID.randomUUID());
        order.setClient(client);
        order.setManager(manager);
        order.setCarId(carId);
        order.setStatus(OrderStatusInStock.CREATED);

        OrderInStock savedOrder = orderRepository.save(order);
        log.info("Created IN_STOCK order: {} for car: {}", savedOrder.getId(), carId);
        return orderMapper.toDto(savedOrder);
    }

    @Transactional
    public OrderDto createOrderCustom(UUID clientId, UUID carModelId, Map<ComponentType, UUID> componentIds) {
        User client = userRepository.findById(clientId)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + clientId));

        CarModel carModel = carModelRepository.findById(carModelId)
                .orElseThrow(() -> new EntityNotFoundException("Car model not found: " + carModelId));

        Map<ComponentType, Component> components = new HashMap<>();
        for (Map.Entry<ComponentType, UUID> entry : componentIds.entrySet()) {
            Component component = componentRepository.findById(entry.getValue())
                    .orElseThrow(() -> new EntityNotFoundException("Component not found: " + entry.getValue()));
            components.put(entry.getKey(), component);
        }

        ConfigurationResult configResult = configurator.validateConfiguration(carModel, components);

        if (!configResult.isValid()) {
            throw new DomainValidationException("Invalid configuration: " + configResult.getErrorMessage());
        }

        User manager = assignRandomManager();

        OrderCustom order = new OrderCustom();
        order.setId(UUID.randomUUID());
        order.setClient(client);
        order.setManager(manager);
        order.setCarModel(carModel);
        order.setSelectedComponentIds(componentIds);
        order.setStatus(OrderStatusCustom.CREATED);

        OrderCustom savedOrder = orderRepository.save(order);
        log.info("Created CUSTOM order: {} for model: {}", savedOrder.getId(), carModelId);
        return orderMapper.toDto(savedOrder);
    }

    @Transactional
    public OrderDto updateInStockOrderStatus(UUID orderId, OrderStatusInStock newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found: " + orderId));

        if (!(order instanceof OrderInStock orderInStock)) {
            throw new DomainValidationException("Order is not an in-stock order");
        }

        OrderStatusInStock oldStatus = orderInStock.getStatus();
        orderInStock.setStatus(newStatus);
        Order updatedOrder = orderRepository.save(order);

        // Публикуем событие при переходе в PAID
        if (newStatus == OrderStatusInStock.PAID && oldStatus != OrderStatusInStock.PAID) {
            log.info("Order {} transitioned to PAID, publishing event", orderId);
            eventPublisher.publishOrderSentForApproval(orderInStock);
        }

        return orderMapper.toDto(updatedOrder);
    }

    @Transactional
    public OrderDto updateCustomOrderStatus(UUID orderId, OrderStatusCustom newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found: " + orderId));

        if (!(order instanceof OrderCustom orderCustom)) {
            throw new DomainValidationException("Order is not a custom order");
        }

        OrderStatusCustom oldStatus = orderCustom.getStatus();
        orderCustom.setStatus(newStatus);
        Order updatedOrder = orderRepository.save(order);

        // Публикуем событие при переходе в PAID
        if (newStatus == OrderStatusCustom.PAID && oldStatus != OrderStatusCustom.PAID) {
            log.info("Order {} transitioned to PAID, publishing event", orderId);
            eventPublisher.publishOrderSentForApproval(orderCustom);
        }

        return orderMapper.toDto(updatedOrder);
    }

    public List<OrderDto> findAllInStockOrders() {
        return orderMapper.toDtoList(orderRepository.findAll().stream()
                .filter(order -> order instanceof OrderInStock)
                .map(order -> (OrderInStock) order)
                .collect(Collectors.toList()));
    }

    public List<OrderDto> findAllCustomOrders() {
        return orderMapper.toDtoList(orderRepository.findAll().stream()
                .filter(order -> order instanceof OrderCustom)
                .map(order -> (OrderCustom) order)
                .collect(Collectors.toList()));
    }

    public List<OrderDto> findByClientId(UUID clientId) {
        return orderMapper.toDtoList(orderRepository.findAll().stream()
                .filter(order -> order.getClient().getId().equals(clientId))
                .collect(Collectors.toList()));
    }

    @Transactional
    public void deleteById(UUID orderId) {
        if (!orderRepository.existsById(orderId)) {
            throw new EntityNotFoundException("Order not found: " + orderId);
        }
        orderRepository.deleteById(orderId);
    }

    @Transactional
    public OrderDto update(OrderDto orderDto) {
        Order existingOrder = orderRepository.findById(orderDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Order not found: " + orderDto.getId()));
        orderMapper.updateEntityFromDto(orderDto, existingOrder);
        Order updatedOrder = orderRepository.save(existingOrder);
        return orderMapper.toDto(updatedOrder);
    }

    private User assignRandomManager() {
        return userRepository.findAll().stream()
                .filter(user -> user.getRole() == Role.MANAGER)
                .findFirst()
                .orElseThrow(() -> new DomainValidationException("No manager available"));
    }

    public void assignManager(UUID orderId, UUID managerId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found: " + orderId));

        User manager = userRepository.findById(managerId)
                .orElseThrow(() -> new EntityNotFoundException("Manager not found: " + managerId));

        if (manager.getRole() != Role.MANAGER) {
            throw new DomainValidationException("User is not a manager");
        }

        order.setManager(manager);
        orderRepository.save(order);
    }

    private List<String> extractClientRoles(Jwt jwt) {
        Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
        if (resourceAccess == null) return List.of();

        Map<String, Object> clientResource = (Map<String, Object>) resourceAccess.get("my-car-api");
        if (clientResource == null) return List.of();

        List<String> roles = (List<String>) clientResource.get("roles");
        return roles != null ? roles : List.of();
    }
}
