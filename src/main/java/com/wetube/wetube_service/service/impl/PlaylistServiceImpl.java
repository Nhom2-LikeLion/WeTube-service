package com.wetube.wetube_service.service.impl;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.wetube.wetube_service.dto.PlaylistDto;
import com.wetube.wetube_service.entity.AppUser;
import com.wetube.wetube_service.entity.playlist.Playlist;
import com.wetube.wetube_service.entity.playlist.PlaylistVideo;
import com.wetube.wetube_service.entity.playlist.Video;
import com.wetube.wetube_service.enumeration.PlaylistType;
import com.wetube.wetube_service.mapper.PlaylistMapper;
import com.wetube.wetube_service.repository.PlaylistRepository;
import com.wetube.wetube_service.repository.PlaylistVideoRepository;
import com.wetube.wetube_service.repository.UserRepository;
import com.wetube.wetube_service.repository.VideoRepository;
import com.wetube.wetube_service.service.PlaylistService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class PlaylistServiceImpl implements PlaylistService {
    
    private final PlaylistRepository plr;
    private final PlaylistVideoRepository plvr;
    private final VideoRepository vdr;
    private final UserRepository usr;

    public PlaylistServiceImpl(PlaylistRepository plr,
                               PlaylistVideoRepository plvr,
                               VideoRepository vdr,
                               UserRepository usr) {
        this.plr = plr;
        this.plvr = plvr;
        this.vdr = vdr;
        this.usr = usr;
    }

    @Override
    public List<PlaylistDto> getAllPlaylistByUserId(UUID userId) {
        List<Playlist> playlist = plr.findByUser_Id(userId);

        if (playlist.isEmpty()) {
        throw new RuntimeException("No playlists found for user " + userId);
    }
        return playlist.stream()
            .map(pl -> PlaylistDto.builder()
                .playlistVideoId(null) 
                .videoId(null)
                .videoUrl(null)
                .thumbnailUrl(null)
                .historyDuration(0)
                .build())
            .collect(Collectors.toList());
    }

    @Override
    public PlaylistDto getPlaylistVideoById(UUID playlistVideoId){
        PlaylistVideo pv = plvr.findById(playlistVideoId)
                .orElseThrow(() -> new RuntimeException("PlaylistVideo not found"));
        return PlaylistMapper.toPlaylistDto(pv);
    }

    @Override
    public List<PlaylistDto> getAllPlaylistVideosByPlaylistId(UUID playlistId) {
        Playlist playlist = plr.findById(playlistId)
                .orElseThrow(() -> new RuntimeException("Playlist not found"));
        return playlist.getPlaylistVideos()
                .stream()
                .map(PlaylistMapper::toPlaylistDto) 
                .collect(Collectors.toList());
    }

    @Override
    public Playlist createPlaylist(String title, UUID userId, PlaylistType type) {
        AppUser user = usr.findById(userId)
                .orElseThrow(() -> new RuntimeException("user not found"));
        
        Playlist playlist = Playlist.builder()
                .title(title)
                .user(user)
                .playlistType(type)
                .build();

        return plr.save(playlist);
    }

    @Override
    public PlaylistDto addVideoToPlaylist(UUID playlistId, UUID videoId, float historyDuration) {
        Playlist playlist = plr.findById(playlistId)
                .orElseThrow(() -> new RuntimeException("Playlist not found"));
        Video video = vdr.findById(videoId)
                .orElseThrow(() -> new RuntimeException("Video not found"));

        PlaylistVideo pv = PlaylistVideo.builder()
                .playlist(playlist)
                .video(video)
                .historyDuration(historyDuration)
                .build();

        PlaylistVideo saved = plvr.save(pv);
        return PlaylistMapper.toPlaylistDto(saved); 
    }

    @Override
    public void removeVideoFromPlaylist(UUID playlistVideoId) {
        plvr.deleteById(playlistVideoId);
    }

    @Override
    public void removePlaylist(UUID playlistId){
        if (!plr.existsById(playlistId)) {
        throw new EntityNotFoundException("Playlist not found");
    }
        plr.deleteById(playlistId);
    }

}
