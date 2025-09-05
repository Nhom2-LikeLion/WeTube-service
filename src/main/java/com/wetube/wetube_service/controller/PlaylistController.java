package com.wetube.wetube_service.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wetube.wetube_service.dto.PlaylistDto;
import com.wetube.wetube_service.dto.playlist.CreatePlaylistRequest;
import com.wetube.wetube_service.entity.playlist.Playlist;
import com.wetube.wetube_service.service.PlaylistService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/playlists")
@RequiredArgsConstructor
public class PlaylistController {
    private final PlaylistService pls;

    //  Tạo playlist bằng JSON body
    @PostMapping("/create")
    public ResponseEntity<Playlist> createPlaylist(@RequestBody CreatePlaylistRequest request) {
        return ResponseEntity.ok(pls.createPlaylist(
                request.getTitle(),
                request.getUserId(),
                request.getType()
        ));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PlaylistDto>> getAllPlaylistsByUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(pls.getAllPlaylistByUserId(userId));
    }

    @GetMapping("/video/{playlistVideoId}")
    public ResponseEntity<PlaylistDto> getPlaylistVideoById(@PathVariable UUID playlistVideoId) {
        return ResponseEntity.ok(pls.getPlaylistVideoById(playlistVideoId));
    }

    @GetMapping("/{playlistId}/videos")
    public ResponseEntity<List<PlaylistDto>> getAllVideosInPlaylist(@PathVariable UUID playlistId) {
        return ResponseEntity.ok(pls.getAllPlaylistVideosByPlaylistId(playlistId));
    }

    @PostMapping("/{playlistId}/videos")
    public ResponseEntity<PlaylistDto> addVideoToPlaylist(
            @PathVariable UUID playlistId,
            @RequestParam UUID videoId,
            @RequestParam(defaultValue = "0") float historyDuration) {
        return ResponseEntity.ok(pls.addVideoToPlaylist(playlistId, videoId, historyDuration));
    }

    @DeleteMapping("/video/{playlistVideoId}")
    public ResponseEntity<Void> removeVideoFromPlaylist(@PathVariable UUID playlistVideoId) {
        pls.removeVideoFromPlaylist(playlistVideoId);
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("/{playlistId}")
    public ResponseEntity<Void> removePlaylist(@PathVariable UUID playlistId){
        pls.removePlaylist(playlistId);
        return ResponseEntity.noContent().build();
    }
}
