package com.wetube.wetube_service.mapper;

import com.wetube.wetube_service.dto.request.NotificationRequestDto;
import com.wetube.wetube_service.dto.response.NotificationResponseDto;
import com.wetube.wetube_service.entity.notification.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    NotificationResponseDto toResponse(Notification entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isRead", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Notification toEntity(NotificationRequestDto request);
}

