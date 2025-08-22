package com.wetube.wetube_service.Repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.wetube.wetube_service.model.VideoTag;

public interface VideoTagRepository extends JpaRepository<VideoTag, UUID> {

    @Query("""
        SELECT CASE WHEN COUNT(vt) > 0 THEN TRUE ELSE FALSE END
        FROM VideoTag vt
        WHERE vt.video.id = :videoId AND vt.tag.id = :tagId
    """)
    boolean existsByVideosIdAndTagsId(@Param("videoId") UUID videoId,
                                      @Param("tagId")   UUID tagId);
}
