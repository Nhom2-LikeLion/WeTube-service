package com.wetube.wetube_service.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

import com.wetube.wetube_service.dto.request.CreatePlaylistRequest;
import com.wetube.wetube_service.dto.request.PlaylistaddRequest;
import com.wetube.wetube_service.dto.response.PlaylistUserDto;
import com.wetube.wetube_service.dto.response.UserResponseDto;
import com.wetube.wetube_service.enumeration.PlaylistType;
import com.wetube.wetube_service.service.PlaylistService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/playlists")
@RequiredArgsConstructor
public class PlaylistController {
    private final PlaylistService pls;

//    @PostMapping("/init/{userId}")
//    public ResponseEntity<Void> createPlaylist(@PathVariable UUID userId) {
//        pls.initiatePlaylist(userId);
//        return ResponseEntity.noContent().build();
//    }

    //  Tạo playlist bằng JSON body
    @PostMapping("/create")
    public ResponseEntity<PlaylistUserDto> createPlaylist(@RequestBody CreatePlaylistRequest request) {
        return ResponseEntity.ok(pls.createPlaylist(request));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PlaylistUserDto>> getAllPlaylistsByUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(pls.getAllPlaylistByUserId(userId));
    }
    
    @GetMapping("/{userId}/playlistType")
    public ResponseEntity<List<PlaylistUserDto>> getAllPlaylistsByTagUser(
        @PathVariable UUID userId,
        @RequestParam PlaylistType playlistType) {

        return ResponseEntity.ok(pls.getAllPlaylistByTagUserId(userId,playlistType));
    }

    @GetMapping("/detail/{playlistId}")
    public ResponseEntity<UserResponseDto.PlaylistDetailDto> getPlaylistVideoById(@PathVariable UUID playlistId) {
        return ResponseEntity.ok(pls.getPlaylistVideoById(playlistId));
    }

    @PostMapping("/videos/add")
     public ResponseEntity<UserResponseDto.PlaylistVideoDto> addVideoToPlaylist(@RequestBody PlaylistaddRequest request) {
        return ResponseEntity.ok(pls.addVideoToPlaylist(request));
    }

    @DeleteMapping("/{videoId}/{playlistVideoId}")
    public ResponseEntity<Map<String, String>> removeVideoFromPlaylist(
            @PathVariable UUID videoId,
            @PathVariable UUID playlistVideoId) {

        pls.removeVideoFromPlaylist(videoId, playlistVideoId);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Video removed from playlist successfully");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{playlistId}")
    public ResponseEntity<Map<String, String>> removePlaylist(@PathVariable UUID playlistId){
        pls.removePlaylist(playlistId);
        return ResponseEntity.ok(Map.of("message","Playlist deleted successfully"));
    }

    @GetMapping("/recentlyadded/{userId}")
    public List<PlaylistUserDto> getRecentlyAddedPlaylists(
            @PathVariable UUID userId,
            @RequestParam(defaultValue = "5") int limit
    ) {
        return pls.getRecentlyAddedPlaylists(userId, limit);
    }
}
