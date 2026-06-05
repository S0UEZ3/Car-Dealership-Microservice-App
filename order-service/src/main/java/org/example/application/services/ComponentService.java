package org.example.application.services;

import lombok.RequiredArgsConstructor;
import org.example.application.exceptions.DomainValidationException;
import org.example.application.exceptions.EntityNotFoundException;
import org.example.dataAccess.models.Component;
import org.example.dataAccess.repositories.IComponentRepository;
import org.example.presentation.dto.mapper.ComponentMapper;
import org.example.presentation.dto.objects.ComponentDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ComponentService {
    private final IComponentRepository componentRepository;
    private final ComponentMapper componentMapper;

    @Transactional
    public ComponentDto save(ComponentDto componentDto) {
        Component component = componentMapper.toEntity(componentDto);
        if (component.getId() == null) {
            component.setId(UUID.randomUUID());
        }
        validateNewComponent(component);

        Component savedComponent = componentRepository.save(component);
        return componentMapper.toDto(savedComponent);
    }

    public ComponentDto findById(UUID componentId) {
        return componentMapper.toDto(
                componentRepository.findById(componentId)
                .orElseThrow(() -> new EntityNotFoundException("Component not found: " + componentId)));
    }

    public List<ComponentDto> findAll() {
        return componentMapper.toDtoList(componentRepository.findAll());
    }

    @Transactional
    public ComponentDto update(ComponentDto componentDto) {
        Component existingComponent = componentRepository.findById(componentDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Component not found: " + componentDto.getId()));

        componentMapper.updateEntityFromDto(componentDto, existingComponent);
        validateNewComponent(existingComponent);
        Component updatedComponent = componentRepository.save(existingComponent);
        return componentMapper.toDto(updatedComponent);
    }

    @Transactional
    public void deleteById(UUID componentId) {
        if (!componentRepository.existsById(componentId)) {
            throw new EntityNotFoundException("Component not found: " + componentId);
        }
        componentRepository.deleteById(componentId);
    }

    private void updateComponentFields(Component existing, Component updated) {
        if (updated.getType() != null) {
            existing.setType(updated.getType());
        }
        if (updated.getName() != null) {
            existing.setName(updated.getName());
        }
        if (updated.getDescription() != null) {
            existing.setDescription(updated.getDescription());
        }
        existing.setPrice(updated.getPrice());
        if (updated.getCompatibleModels() != null) {
            existing.setCompatibleModels(updated.getCompatibleModels());
        }
    }

    private void validateNewComponent(Component component) {
        if (component.getType() == null) {
            throw new DomainValidationException("Component type is required");
        }
        if (component.getName() == null || component.getName().trim().isEmpty()) {
            throw new DomainValidationException("Component name is required");
        }
        if (component.getCompatibleModels() == null || component.getCompatibleModels().isEmpty()) {
            throw new DomainValidationException("Component must be compatible with at least one model");
        }
    }
}
