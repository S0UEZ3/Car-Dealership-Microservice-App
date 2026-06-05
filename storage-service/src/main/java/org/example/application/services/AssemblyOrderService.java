package org.example.application.services;

import lombok.RequiredArgsConstructor;
import org.example.dataAccess.models.AssemblyOrder;
import org.example.dataAccess.models.AssemblyOrderStatus;
import org.example.dataAccess.repositories.AssemblyOrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AssemblyOrderService {
    private final AssemblyOrderRepository assemblyOrderRepository;

    @Transactional
    public AssemblyOrder create(AssemblyOrder order) {
        order.setId(UUID.randomUUID());
        order.setStatus(AssemblyOrderStatus.CREATED);
        return assemblyOrderRepository.save(order);
    }

    public List<AssemblyOrder> findAll() {
        return assemblyOrderRepository.findAll();
    }

    public AssemblyOrder findById(UUID id) {
        return assemblyOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Assembly order not found: " + id));
    }

    @Transactional
    public AssemblyOrder update(AssemblyOrder order) {
        return assemblyOrderRepository.save(order);
    }

    @Transactional
    public void deleteById(UUID id) {
        assemblyOrderRepository.deleteById(id);
    }
}
