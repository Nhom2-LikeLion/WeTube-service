package com.wetube.wetube_service.job;

import com.wetube.wetube_service.entity.video.VideoTag;
import com.wetube.wetube_service.repository.video.VideoRepository;
import com.wetube.wetube_service.repository.video.VideoSearchRepository;
import com.wetube.wetube_service.search.VideoDocument;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Component
public class ElasticBootstrapLoader implements CommandLineRunner {

    private final VideoRepository videoRepository;
    private final VideoSearchRepository videoSearchRepository;

    public ElasticBootstrapLoader(VideoRepository videoRepository,
                                  VideoSearchRepository videoSearchRepository) {
        this.videoRepository = videoRepository;
        this.videoSearchRepository = videoSearchRepository;
    }

    @Override
    @Transactional(readOnly = true) 
    public void run(String... args) {
        System.out.println("🚀 Bắt đầu sync dữ liệu cũ MySQL → Elasticsearch...");

        var videos = videoRepository.findAll();

        var docs = videos.stream().map(v -> new VideoDocument(
                v.getId().toString(),
                v.getTitle(),
                v.getDescription(),
                v.getUser().getId().toString(),

                // lấy tên tag từ VideoTag
                v.getVideoTags().stream()
                        .map(VideoTag::getTag)        // lấy entity Tag
                        .map(tag -> tag.getName())    // lấy tên Tag
                        .collect(Collectors.toList()),

                java.util.List.of(),

                v.getCreatedAt().toLocalDate()
        )).toList();

        videoSearchRepository.saveAll(docs);

        System.out.println("✅ Sync xong " + docs.size() + " video(s) vào Elasticsearch!");
    }
}
