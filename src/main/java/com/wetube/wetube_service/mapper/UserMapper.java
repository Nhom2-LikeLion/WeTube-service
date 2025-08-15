package com.wetube.wetube_service.mapper;

import com.wetube.wetube_service.dto.response.UserResponseDto;
import com.wetube.wetube_service.entity.AppUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponseDto toDto(AppUser entity);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "clerkId", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "channel.status", ignore = true)
    @Mapping(target = "channel.revenue", ignore = true)
    @Mapping(target = "channel.updatedAt", ignore = true)
    AppUser toEntity(UserResponseDto dto);
}
