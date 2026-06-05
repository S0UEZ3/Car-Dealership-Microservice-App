package org.example.presentation.dto.mapper;

import org.example.dataAccess.models.CarModel;
import org.example.presentation.dto.objects.CarModelDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CarModelMapper {

    CarModelDto toDto(CarModel carModel);

    CarModel toEntity(CarModelDto carModelDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "removed", ignore = true)
    void updateEntityFromDto(CarModelDto dto, @MappingTarget CarModel entity);

    List<CarModelDto> toDtoList(List<CarModel> carModels);
}
