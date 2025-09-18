package com.wetube.wetube_service.entity.notification;

import com.wetube.wetube_service.entity.channel.Channel;
import com.wetube.wetube_service.entity.video.Video;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id")
    private Channel channel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "video_id")
    private Video video;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    private String message;

    private boolean isRead = false;

    private LocalDateTime createdAt = LocalDateTime.now();

    public enum NotificationType {
        NEW_VIDEO,
        COMMENT,
        REPLY,
        SYSTEM
    }
}
