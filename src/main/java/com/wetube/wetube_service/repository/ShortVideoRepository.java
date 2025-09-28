package com.wetube.wetube_service.repository;

import com.wetube.wetube_service.entity.shorts.ShortsVideo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ShortVideoRepository extends JpaRepository<ShortsVideo, UUID> {
    List<ShortsVideo> findByChannelId(UUID channelId);

    @Query(value = "SELECT * FROM short_videos ORDER BY RAND() LIMIT :limit", nativeQuery = true)
    List<ShortsVideo> findRandomShorts(@Param("limit") int limit);
}
