package org.example.presentation.dto.mapper;

import org.example.dataAccess.models.TestDriveRequest;
import org.example.presentation.dto.objects.TestDriveRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {UserMapper.class})
public interface TestDriveRequestMapper {

    TestDriveRequestDto toDto(TestDriveRequest request);

    TestDriveRequest toEntity(TestDriveRequestDto requestDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "removed", ignore = true)
    void updateEntityFromDto(TestDriveRequestDto dto, @MappingTarget TestDriveRequest entity);

    List<TestDriveRequestDto> toDtoList(List<TestDriveRequest> requests);
}
