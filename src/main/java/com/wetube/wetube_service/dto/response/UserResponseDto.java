package com.wetube.wetube_service.dto.response;

import com.wetube.wetube_service.enumeration.PlaylistType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDto {
    private String email;
    private String name;
    private String picture;
    private LocalDateTime createdAt;
    private UserChannelResponseDto channel;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class PlaylistDetailDto {
        private UUID playlistId;
        private String playlistTitle;
        private PlaylistType playlistType;
        private int totalVideos;
        private LocalDateTime createdAt;
        private List<PlaylistVideoDto> videos;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PlaylistVideoDto {
        private UUID videoId;
        private String videoTitle;
        private String videoUrl;
        private String thumbnailUrl;
        private float historyDuration;
    }
}
