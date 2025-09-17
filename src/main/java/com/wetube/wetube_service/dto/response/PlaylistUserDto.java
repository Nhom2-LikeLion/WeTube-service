package com.wetube.wetube_service.dto.response;

import java.time.LocalDate;
import java.util.UUID;

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
public class PlaylistUserDto {
    private UUID playlistId;
    private String playlistTitle;
    private PlaylistType playlistType;
    private int totalVideos;
    private LocalDate createdAt;
    private String privacy;   
    private String lastUpdatedLabel;
    private String thumbnailUrl; 
}
