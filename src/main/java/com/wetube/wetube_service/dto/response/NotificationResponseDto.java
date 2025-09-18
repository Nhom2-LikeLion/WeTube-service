package com.wetube.wetube_service.dto.response;

import com.wetube.wetube_service.entity.notification.Notification;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationResponseDto {
    private UUID id;
    private String message;
    private Notification.NotificationType type;
    private boolean isRead;
    private LocalDateTime createdAt;
    private UUID videoId;
    private ChannelResponseDto channel;
}
