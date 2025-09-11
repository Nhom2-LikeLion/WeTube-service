package com.wetube.wetube_service.service;

import com.wetube.wetube_service.entity.video.Video;
import com.wetube.wetube_service.mapper.video.VideoMapper;
import com.wetube.wetube_service.repository.video.VideoRepository;
import com.wetube.wetube_service.repository.video.VideoSearchRepository;
import com.wetube.wetube_service.search.VideoDocument;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class DataMigrationService {
    private final VideoSearchRepository videoSearchRepository;
    private final VideoRepository videoRepository;
    private final VideoMapper videoMapper;

    @Transactional(readOnly = true)
    public void importAllVideosToElasticsearch() {
        log.info("Starting import of all videos to Elasticsearch...");

        int pageSize = 100;
        int pageNumber = 0;
        long totalImported = 0;

        while (true) {
            Pageable pageable = PageRequest.of(pageNumber, pageSize);
            Page<Video> videoPage = videoRepository.findAll(pageable);

            if (videoPage.isEmpty()) {
                break;
            }

            List<VideoDocument> document = videoPage.getContent()
                    .stream()
                    .map(videoMapper::mapEntityToVideoDocument)
                    .toList();
            videoSearchRepository.saveAll(document);
            totalImported += document.size();

            log.info("Imported page {} with {} videos. Total imported so far: {}", pageNumber + 1, document.size(),
                    totalImported);
            pageNumber++;

        }
        log.info("Completed import of all videos to Elasticsearch. Total videos imported: {}", totalImported);
    }

    @Transactional(readOnly = true)
    public void importVideosByIds(List<UUID> videoIds) {
        log.info("Starting import of {} products by IDs to Elasticsearch...", videoIds.size());

        List<Video> video = videoRepository.findAllById(videoIds);
        List<VideoDocument> documents = video.stream()
                .map(videoMapper::mapEntityToVideoDocument)
                .toList();

        videoSearchRepository.saveAll(documents);
        log.info("Imported {} products to Elasticsearch", documents.size());
    }

    @Transactional(readOnly = true)
    public void importVideoByTitle(String title) {
        log.info("Starting import of products in category '{}' to Elasticsearch...", title);

        List<Video> videos = videoRepository.findAll().stream()
                .filter(video -> title.equals(video.getTitle()))
                .toList();

        List<VideoDocument> documents = videos.stream()
                .map(videoMapper::mapEntityToVideoDocument)
                .toList();

        videoSearchRepository.saveAll(documents);
        log.info("Imported {} products in category '{}' to Elasticsearch", documents.size(), title);
    }

    public void clearElasticsearchData() {
        log.info("Clearing all data from Elasticsearch...");
        videoSearchRepository.deleteAll();
        log.info("Elasticsearch data cleared");
    }

    public long getElasticsearchDocumentCount() {
        return videoSearchRepository.count();
    }

    @Transactional(readOnly = true)
    public long getMySQLVideoCount() {
        return videoRepository.count();
    }

}
