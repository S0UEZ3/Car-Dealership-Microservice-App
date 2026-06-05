package org.example.presentation.dto.mapper;

import org.example.dataAccess.models.User;
import org.example.presentation.dto.objects.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    UserDto toDto(User user);

    User toEntity(UserDto userDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "removed", ignore = true)
    void updateEntityFromDto(UserDto dto, @MappingTarget User entity);

    List<UserDto> toDtoList(List<User> users);
}
