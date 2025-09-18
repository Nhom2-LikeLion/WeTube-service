package com.wetube.wetube_service.service.notification;

import com.wetube.wetube_service.dto.response.NotificationResponseDto;

import java.util.List;
import java.util.UUID;

public interface NotificationService {
    List<NotificationResponseDto> getUserNotifications(UUID userId);

    void markAsRead(UUID notificationId);

    void markAllAsRead(UUID userId);

    void deleteNotification(UUID notificationId);

    void createNewVideoNotification(UUID videoId, UUID channelId);
}

