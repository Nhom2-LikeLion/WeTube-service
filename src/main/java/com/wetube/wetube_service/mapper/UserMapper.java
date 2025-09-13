package com.wetube.wetube_service.mapper;

import com.wetube.wetube_service.dto.UserDto;
import com.wetube.wetube_service.dto.response.UserResponseDto;
import com.wetube.wetube_service.entity.AppUser;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    AppUser toEntity(UserDto userDto);
    UserResponseDto toDto(AppUser entity);
}
