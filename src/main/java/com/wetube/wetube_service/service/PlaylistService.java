package com.wetube.wetube_service.service;

import java.util.List;
import java.util.UUID;

import com.wetube.wetube_service.dto.PlaylistDto;
import com.wetube.wetube_service.entity.playlist.Playlist;
import com.wetube.wetube_service.enumeration.PlaylistType;





public interface PlaylistService {
    List<PlaylistDto> getAllPlaylistByUserId(UUID userId);
    PlaylistDto getPlaylistVideoById(UUID playlistVideoId);
    List<PlaylistDto> getAllPlaylistVideosByPlaylistId(UUID playlistId);
    Playlist createPlaylist(String title, UUID userId, PlaylistType type); 
    PlaylistDto addVideoToPlaylist(UUID playlistId, UUID videoId, float historyDuration);
    void removeVideoFromPlaylist(UUID playlistVideoId);
    void removePlaylist(UUID playlistId);
}
