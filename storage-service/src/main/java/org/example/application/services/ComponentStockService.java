package org.example.application.services;

import lombok.RequiredArgsConstructor;
import org.example.dataAccess.models.ComponentStock;
import org.example.dataAccess.repositories.ComponentStockRepository;
import org.example.presentation.dto.objects.ReservationRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ComponentStockService {
    private final ComponentStockRepository componentStockRepository;

    public List<ComponentStock> findAll() {
        return componentStockRepository.findAll();
    }

    public ComponentStock findById(UUID id) {
        return componentStockRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Part stock not found: " + id));
    }

    public ComponentStock findByComponentId(UUID componentId) {
        return componentStockRepository.findByComponentId(componentId)
                .orElseThrow(() -> new RuntimeException("Part stock not found for component: " + componentId));
    }

    public boolean isPartAvailable(UUID componentId, Integer quantity) {
        return componentStockRepository.findByComponentId(componentId)
                .map(stock -> stock.getQuantity() >= quantity)
                .orElse(false);
    }

    @Transactional
    public ComponentStock addPart(ComponentStock partStock) {
        if (componentStockRepository.existsByComponentId(partStock.getComponentId())) {
            throw new RuntimeException("Part stock already exists for component: " + partStock.getComponentId());
        }
        if (partStock.getQuantity() == null) {
            partStock.setQuantity(0);
        }
        if (partStock.getReservedQuantity() == null) {
            partStock.setReservedQuantity(0);
        }
        return componentStockRepository.save(partStock);
    }

    @Transactional
    public ComponentStock addQuantity(UUID id, Integer quantity) {
        ComponentStock stock = findById(id);
        stock.setQuantity(stock.getQuantity() + quantity);
        return componentStockRepository.save(stock);
    }

    @Transactional
    public void reserveParts(List<ReservationRequest> requests) {
        for (ReservationRequest request : requests) {
            ComponentStock stock = findByComponentId(request.getComponentId());
            int available = stock.getQuantity() - stock.getReservedQuantity();
            if (available < request.getQuantity()) {
                throw new RuntimeException("Not enough parts for component: " + request.getComponentId() +
                        ". Available: " + available + ", Requested: " + request.getQuantity());
            }
            stock.setReservedQuantity(stock.getReservedQuantity() + request.getQuantity());
            componentStockRepository.save(stock);
        }
    }

    @Transactional
    public void releaseParts(List<ReservationRequest> requests) {
        for (ReservationRequest request : requests) {
            ComponentStock stock = findByComponentId(request.getComponentId());
            stock.setReservedQuantity(stock.getReservedQuantity() - request.getQuantity());
            componentStockRepository.save(stock);
        }
    }

    @Transactional
    public void deductParts(List<ReservationRequest> requests) {
        for (ReservationRequest request : requests) {
            ComponentStock stock = findByComponentId(request.getComponentId());
            stock.setQuantity(stock.getQuantity() - request.getQuantity());
            stock.setReservedQuantity(stock.getReservedQuantity() - request.getQuantity());
            componentStockRepository.save(stock);
        }
    }

    @Transactional
    public ComponentStock update(ComponentStock partStock) {
        return componentStockRepository.save(partStock);
    }

    @Transactional
    public void deleteById(UUID id) {
        componentStockRepository.deleteById(id);
    }
}
