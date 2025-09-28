package com.wetube.wetube_service.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShortVideoDto {
    private UUID id;
    private String title;
    private String description;
    private String videoUrl;
    private String thumbnailUrl;
    private int views;
    private int likes;

    private UUID channelId;
    private String channelName;
    private String channelPicture;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

