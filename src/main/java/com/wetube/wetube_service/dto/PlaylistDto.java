package com.wetube.wetube_service.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.wetube.wetube_service.enumeration.PlaylistType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlaylistDto {
    private UUID playlistVideoId; 
    private UUID videoId;
    private String videoTitle;
    private String videoUrl;
    private String thumbnailUrl;
    private float historyDuration;

    private UUID playlistId;
    private String playlistTitle;
    private PlaylistType playlistType;
    private int totalVideos;
    private LocalDateTime createdAt;
}
