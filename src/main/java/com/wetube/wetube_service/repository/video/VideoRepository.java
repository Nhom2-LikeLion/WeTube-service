package com.wetube.wetube_service.repository.video;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.wetube.wetube_service.entity.video.Video;
import com.wetube.wetube_service.enumeration.ActiveStatus;

public interface VideoRepository extends JpaRepository<Video, UUID> {
    Video findByIdAndUser_Id(UUID videoId, UUID userId);

    @Query("""
        SELECT v FROM Video v
        LEFT JOIN FETCH v.videoTags vt
        LEFT JOIN FETCH vt.tag t
        WHERE v.id = :id
    """)
    Optional<Video> findByIdWithTags(@Param("id") UUID id);
    List<Video> findAllByVideosStatusOrderByCreatedAtDesc(ActiveStatus status);

    List<Video> findByTitleContainingIgnoreCase(String title);
    Page<Video> findByTitleContainingIgnoreCase(String title, Pageable pageable);
    Page<Video> findAll(Pageable pageable);
    List<Video> findDistinctByVideoTags_Tag_NameInAndIdNot(List<String> tagNames, UUID excludeId);


    


}
