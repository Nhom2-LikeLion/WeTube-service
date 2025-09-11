package com.wetube.wetube_service.repository.video;

import com.wetube.wetube_service.search.VideoDocument;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface VideoSearchRepository extends ElasticsearchRepository<VideoDocument, String> {
    Page<VideoDocument> findByTags(String tag, Pageable pageable);
}
