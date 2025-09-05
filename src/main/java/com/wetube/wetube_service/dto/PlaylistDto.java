package com.wetube.wetube_service.dto;

import java.util.UUID;
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
public class PlaylistDto {
    private UUID playlistVideoId; 
    private UUID videoId;
    private String videoTitle;
    private String videoUrl;
    private String thumbnailUrl;
    private float historyDuration;
}
