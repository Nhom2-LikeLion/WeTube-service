package com.wetube.wetube_service.dto.playlist;

import java.util.UUID;

import com.wetube.wetube_service.enumeration.PlaylistType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatePlaylistRequest {
    private String title;
    private UUID userId;
    private PlaylistType type;
}
