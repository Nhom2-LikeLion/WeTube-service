package com.wetube.wetube_service.service;

import java.util.List;
import java.util.UUID;

import com.wetube.wetube_service.dto.request.CreatePlaylistRequest;
import com.wetube.wetube_service.dto.request.PlaylistaddRequest;
import com.wetube.wetube_service.dto.response.playlist.PlaylistDetailDto;
import com.wetube.wetube_service.dto.response.playlist.UserPlaylistDto;
import com.wetube.wetube_service.dto.response.playlist.PlaylistVideoDto;
import com.wetube.wetube_service.enumeration.PlaylistType;
import com.wetube.wetube_service.exception.DuplicatePlaylistTitleException;


public interface PlaylistService {
    UserPlaylistDto createPlaylist(CreatePlaylistRequest dto);

    List<UserPlaylistDto> getAllPlaylistByUserId(UUID userId);

    List<UserPlaylistDto> getUserPlaylistById(UUID userId);

    List<UserPlaylistDto> getUserCreatedPlaylistById(UUID userId);

    PlaylistDetailDto getPlaylistDetailedById(UUID playlistVideoId);
    PlaylistDetailDto getPlaylistDetailedById(UUID channelId, String playlistName);

    void initiatePlaylist(UUID userId);

    PlaylistVideoDto addVideoToPlaylist(PlaylistaddRequest dto);

    void removeVideoFromPlaylist(UUID videoId, UUID playlistVideoId);

    void removePlaylist(UUID playlistId);


    String getTopViewUserUploaded(UUID userId);

}
