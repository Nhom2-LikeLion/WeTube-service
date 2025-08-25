package com.wetube.wetube_service.Repository;


import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.wetube.wetube_service.entity.Video;

public interface VideoRepository extends JpaRepository<Video, UUID> {
    Video findByIdAndUsersId(UUID id, String usersId);

    @Query("""
        SELECT v FROM Video v
        LEFT JOIN FETCH v.videoTags vt
        LEFT JOIN FETCH vt.tag t
        WHERE v.id = :id
    """)
    Optional<Video> findByIdWithTags(@Param("id") UUID id);
}