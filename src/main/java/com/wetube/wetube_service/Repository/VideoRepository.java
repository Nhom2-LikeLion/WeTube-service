package com.wetube.wetube_service.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wetube.wetube_service.entity.playlist.Video;

public interface VideoRepository extends JpaRepository<Video, UUID>{
    List<Video> findByTitleContainingIgnoreCase(String title);
}
