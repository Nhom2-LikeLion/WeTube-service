package com.wetube.wetube_service.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.wetube.wetube_service.entity.playlist.Playlist;
import com.wetube.wetube_service.enumeration.PlaylistType;

@Repository
public interface PlaylistRepository extends CrudRepository<Playlist, UUID>{
     List<Playlist> findByUser_Id(UUID userId);
     List<Playlist> findByUser_IdAndPlaylistType(UUID userId, PlaylistType playlistType);

}
