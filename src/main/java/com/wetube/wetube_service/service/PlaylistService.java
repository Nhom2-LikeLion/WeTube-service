package com.wetube.wetube_service.service;

import java.util.List;
import java.util.UUID;

import com.wetube.wetube_service.dto.PlaylistDetailDto;
import com.wetube.wetube_service.dto.PlaylistDto;
import com.wetube.wetube_service.dto.request.CreatePlaylistRequest;
import com.wetube.wetube_service.dto.request.PlaylistaddRequest;
import com.wetube.wetube_service.dto.response.PlaylistUserDto;
import com.wetube.wetube_service.enumeration.PlaylistType;


public interface PlaylistService {
    List<PlaylistUserDto> getAllPlaylistByUserId(UUID userId);

    PlaylistDetailDto getPlaylistVideoById(UUID playlistVideoId);
    
    PlaylistUserDto  createPlaylist(CreatePlaylistRequest dto); 

    PlaylistDto addVideoToPlaylist(PlaylistaddRequest dto);

    void removeVideoFromPlaylist(UUID playlistVideoId);
    
    void removePlaylist(UUID playlistId);

    List<PlaylistUserDto> getAllPlaylistByTagUserId(UUID userId,PlaylistType playlistType);
}
