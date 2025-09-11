package com.wetube.wetube_service.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.wetube.wetube_service.dto.PlaylistDetailDto;
import com.wetube.wetube_service.dto.PlaylistDto;
import com.wetube.wetube_service.dto.request.CreatePlaylistRequest;
import com.wetube.wetube_service.dto.request.PlaylistaddRequest;
import com.wetube.wetube_service.dto.response.PlaylistUserDto;
import com.wetube.wetube_service.entity.AppUser;
import com.wetube.wetube_service.entity.Video;
import com.wetube.wetube_service.entity.playlist.Playlist;
import com.wetube.wetube_service.entity.playlist.PlaylistVideo;
import com.wetube.wetube_service.enumeration.PlaylistType;
import com.wetube.wetube_service.exception.ResourceNotFoundException;
import com.wetube.wetube_service.mapper.PlaylistMapper;
import com.wetube.wetube_service.repository.PlaylistRepository;
import com.wetube.wetube_service.repository.PlaylistVideoRepository;
import com.wetube.wetube_service.repository.UserRepository;
import com.wetube.wetube_service.repository.VideoRepository;
import com.wetube.wetube_service.service.PlaylistService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlaylistServiceImpl implements PlaylistService {

    private final PlaylistRepository plr;
    private final PlaylistVideoRepository plvr;
    private final VideoRepository vdr;
    private final UserRepository usr;
    private final PlaylistMapper playlistMapper; 

    @Override
    public List<PlaylistUserDto> getAllPlaylistByUserId(UUID userId) {
        List<Playlist> playlists = plr.findByUser_Id(userId);
        if (playlists.isEmpty()) {
            throw new ResourceNotFoundException("userId", "id", userId.toString());
        }
        return playlistMapper.toDtoList(playlists);
    }

    @Override
    public List<PlaylistUserDto> getAllPlaylistByTagUserId(UUID userId, PlaylistType playlistType) {
        List<Playlist> playlists = plr.findByUser_IdAndPlaylistType(userId, playlistType);
        if (playlists.isEmpty()) {
            throw new ResourceNotFoundException("userId exists or playlistType error", "id", userId.toString());
        }
        return playlistMapper.toDtoList(playlists);
    }

    @Override
    public PlaylistDetailDto getPlaylistVideoById(UUID playlistVideoId) {
        PlaylistVideo pv = plvr.findById(playlistVideoId)
                .orElseThrow(() -> new ResourceNotFoundException("PlaylistVideo", "id", playlistVideoId.toString()));
        Playlist playlist = pv.getPlaylist();
        return playlistMapper.toDetailDto(playlist);
    }

    @Override
    public PlaylistUserDto createPlaylist(CreatePlaylistRequest dto) {
        AppUser user = usr.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("user", "id", dto.getUserId().toString()));

        Playlist playlist = playlistMapper.toEntity(dto, user);
        Playlist saved = plr.save(playlist);

        return playlistMapper.toDto(saved);
    }

   @Override
    public PlaylistDto addVideoToPlaylist(PlaylistaddRequest dto) {
        Playlist playlist = plr.findById(dto.getPlaylistId())
                .orElseThrow(() -> new ResourceNotFoundException("Playlist", "id", dto.getPlaylistId().toString()));
        Video video = vdr.findById(dto.getVideoId())
                .orElseThrow(() -> new ResourceNotFoundException("Video", "id", dto.getVideoId().toString()));

        PlaylistVideo pv = playlistMapper.toPlaylistVideo(dto, playlist, video);
        PlaylistVideo saved = plvr.save(pv);

        return playlistMapper.toPlaylistDto(saved);
    }

    @Override
    public void removeVideoFromPlaylist(UUID playlistVideoId) {
        if (!plvr.existsById(playlistVideoId)) {
            throw new ResourceNotFoundException("PlaylistVideo", "id", playlistVideoId.toString());
        }
        plvr.deleteById(playlistVideoId);
    }

    @Override
    public void removePlaylist(UUID playlistId) {
        if (!plr.existsById(playlistId)) {
            throw new ResourceNotFoundException("Playlist", "id", playlistId.toString());
        }
        plr.deleteById(playlistId);
    }
}
