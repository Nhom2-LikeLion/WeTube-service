package com.wetube.wetube_service.service.video.impl;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.wetube.wetube_service.repository.video.TagRepository;
import com.wetube.wetube_service.repository.video.VideoRepository;
import com.wetube.wetube_service.repository.video.VideoTagRepository;
import com.wetube.wetube_service.dto.video.VideoDto;
import com.wetube.wetube_service.entity.video.Video;
import com.wetube.wetube_service.entity.video.VideoTag;
import com.wetube.wetube_service.mapper.video.VideoMapper;
import com.wetube.wetube_service.entity.video.Tag;
import com.wetube.wetube_service.service.CloudinaryService;
import com.wetube.wetube_service.service.video.VideoService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VideoServiceImpl implements VideoService {

    private final VideoRepository videoRepository;
    private final TagRepository tagRepository;
    private final VideoTagRepository videoTagRepository;
    private final VideoMapper videoMapper;
    private final CloudinaryService cloudinaryService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public VideoDto createVideo(MultipartFile videoFile,
            MultipartFile thumbnailFile,
            VideoDto videoDto) throws Exception {

        if (videoFile == null || videoFile.isEmpty()) {
            throw new IllegalArgumentException("Video file is required");
        }

        String videoUrl = cloudinaryService.uploadVideo(videoFile);
        String thumbnailUrl = (thumbnailFile != null && !thumbnailFile.isEmpty())
                ? cloudinaryService.uploadThumbnail(thumbnailFile)
                : null;

        Video entity = videoMapper.toEntity(videoDto);
        entity.setVideoUrl(videoUrl);
        entity.setThumbnailUrl(thumbnailUrl);

        LocalDateTime now = LocalDateTime.now();
        if (entity.getCreatedAt() == null)
            entity.setCreatedAt(now);
        if (entity.getVideosStatus() == null || entity.getVideosStatus().isBlank()) {
            entity.setVideosStatus("pending");
        }
        entity.setUpdatedAt(now);

        Video saved = videoRepository.save(entity);
        videoRepository.flush();

        return videoMapper.toDto(saved);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public VideoDto addTags(UUID videoId, String hashtagText) {
        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new IllegalArgumentException("Video not found with id: " + videoId));

        Set<String> names = parseHashtagText(hashtagText);

        for (String name : names) {
            Tag tag = tagRepository.findByNameIgnoreCase(name).orElse(null);
            if (tag == null) {
                tag = new Tag();
                tag.setName(name);
                tag.setCount(0);
                tag.setCreatedAt(LocalDateTime.now());
                tag = tagRepository.save(tag);
            }

            if (!videoTagRepository.existsByVideo_IdAndTag_Id(video.getId(), tag.getId())) {
                VideoTag vt = new VideoTag();
                vt.setVideo(video);
                vt.setTag(tag);
                videoTagRepository.save(vt);

                video.getVideoTags().add(vt);

                tag.setCount((tag.getCount() == null ? 0 : tag.getCount()) + 1);
                tagRepository.save(tag);
            }
        }

        Video dtoSource = videoRepository.findByIdWithTags(videoId)
                .orElseThrow(() -> new IllegalArgumentException("Video not found with id: " + videoId));
        return videoMapper.toDto(dtoSource);
    }

    @Override
    @Transactional(readOnly = true)
    public VideoDto getById(String id) {
        UUID uuid = UUID.fromString(id);
        Video video = videoRepository.findById(uuid)
                .orElseThrow(() -> new IllegalArgumentException("Video not found with id: " + id));
        return videoMapper.toDto(video);
    }

    @Override
    @Transactional(readOnly = true)
    public java.util.List<VideoDto> getAllVideo() {
        java.util.List<Video> videos = videoRepository.findAll();
        return videoMapper.toDtoList(videos);
    }

    private Set<String> parseHashtagText(String hashtagText) {
        if (hashtagText == null || hashtagText.isBlank())
            return java.util.Collections.emptySet();
        String[] parts = hashtagText.split("\\s+");
        Set<String> out = new LinkedHashSet<>();
        for (String raw : parts) {
            if (raw == null)
                continue;
            String s = raw.trim();
            if (s.startsWith("#"))
                s = s.substring(1);
            s = s.trim().toLowerCase(Locale.ROOT);
            if (!s.isBlank())
                out.add(s);
        }
        return out;
    }

    @Override
    public List<VideoDto> searchByTitle(String title) {
        return videoRepository.findByTitleContainingIgnoreCase(title)
                .stream().map(videoMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VideoDto> searchByTitlePaging(String title, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return videoRepository.findByTitleContainingIgnoreCase(title, pageable)
                .map(videoMapper::toDto);
    }

    @Override
    public Page<VideoDto> getAllVideosPaging(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return videoRepository.findAll(pageable).map(videoMapper::toDto);
    }
}
