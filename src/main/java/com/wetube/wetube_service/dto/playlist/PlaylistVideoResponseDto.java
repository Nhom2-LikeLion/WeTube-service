package com.wetube.wetube_service.dto.playlist;

import lombok.*;

import java.util.UUID;

import com.wetube.wetube_service.dto.VideoDto;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaylistVideoResponseDto {
    private UUID id;
    private VideoDto video; 
    private float historyDuration;
}
