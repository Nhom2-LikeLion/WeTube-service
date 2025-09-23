package com.wetube.wetube_service.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wetube.wetube_service.entity.playlist.Playlist;
import com.wetube.wetube_service.entity.playlist.PlaylistVideo;
import com.wetube.wetube_service.enumeration.PlaylistType;

@Repository
public interface PlaylistVideoRepository extends JpaRepository<PlaylistVideo, UUID>{
    Optional<PlaylistVideo> findByPlaylist_IdAndVideo_Id(UUID playlistId, UUID videoId);
    List<PlaylistVideo> findByPlaylist_IdOrderByUpdatedAtDesc(UUID playlistId);

}
