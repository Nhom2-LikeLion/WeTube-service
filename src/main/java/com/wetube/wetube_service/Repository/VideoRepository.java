package com.wetube.wetube_service.Repository;


import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wetube.wetube_service.model.Video;

public interface VideoRepository extends JpaRepository<Video, UUID> {
    Video findByIdAndUsersId(UUID id, String usersId);
}