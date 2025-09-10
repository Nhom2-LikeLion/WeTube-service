package com.wetube.wetube_service.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wetube.wetube_service.entity.playlist.Playlist;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, UUID>{
     List<Playlist> findByUser_Id(UUID userId);
}
