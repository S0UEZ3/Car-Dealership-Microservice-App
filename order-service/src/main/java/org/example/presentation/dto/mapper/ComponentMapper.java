package org.example.presentation.dto.mapper;

import org.example.dataAccess.models.*;
import org.example.presentation.dto.objects.ComponentDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.Map;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {CarModelMapper.class})
public interface ComponentMapper {

    ComponentDto toDto(Component component);

    Component toEntity(ComponentDto componentDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "removed", ignore = true)
    void updateEntityFromDto(ComponentDto dto, @MappingTarget Component entity);

    List<ComponentDto> toDtoList(List<Component> components);

    Map<ComponentType, ComponentDto> toDtoMap(Map<ComponentType, Component> components);

    Map<ComponentType, Component> toEntityMap(Map<ComponentType, ComponentDto> componentDtos);
}
