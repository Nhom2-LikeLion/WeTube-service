package com.wetube.wetube_service.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wetube.wetube_service.entity.playlist.PlaylistVideo;

@Repository
public interface PlaylistVideoRepository extends JpaRepository<PlaylistVideo, UUID>{
    List<PlaylistVideo> findByPlaylistId(UUID playlistId);
    List<PlaylistVideo> findByPlaylist_Id(UUID playlistId);
    void deleteByPlaylistId(UUID playlistId);
}
