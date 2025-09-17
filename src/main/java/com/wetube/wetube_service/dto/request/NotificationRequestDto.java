package com.wetube.wetube_service.dto.request;

import com.wetube.wetube_service.entity.notification.Notification;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationRequestDto {
    private UUID userId;
    private UUID channelId;
    private UUID videoId;
    private Notification.NotificationType type;
    private String message;
}

