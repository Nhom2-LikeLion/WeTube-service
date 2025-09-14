package com.wetube.wetube_service.service;

import java.util.List;
import java.util.UUID;

import com.wetube.wetube_service.dto.request.CreatePlaylistRequest;
import com.wetube.wetube_service.dto.request.PlaylistaddRequest;
import com.wetube.wetube_service.dto.response.PlaylistUserDto;
import com.wetube.wetube_service.dto.response.UserResponseDto;
import com.wetube.wetube_service.enumeration.PlaylistType;
import org.springframework.data.domain.PageRequest;


public interface PlaylistService {
    List<PlaylistUserDto> getAllPlaylistByUserId(UUID userId);

    UserResponseDto.PlaylistDetailDto getPlaylistVideoById(UUID playlistVideoId);

    PlaylistUserDto  createPlaylist(CreatePlaylistRequest dto);

    void initiatePlaylist(UUID userId);

    UserResponseDto.PlaylistVideoDto addVideoToPlaylist(PlaylistaddRequest dto);

    void removeVideoFromPlaylist(UUID videoId, UUID playlistVideoId);

    void removePlaylist(UUID playlistId);

    List<PlaylistUserDto> getAllPlaylistByTagUserId(UUID userId,PlaylistType playlistType);

    String getTopViewUserUploaded(UUID userId);

}
