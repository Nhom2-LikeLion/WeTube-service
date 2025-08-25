package com.wetube.wetube_service.service.impl;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.wetube.wetube_service.Repository.TagRepository;
import com.wetube.wetube_service.Repository.VideoRepository;
import com.wetube.wetube_service.Repository.VideoTagRepository;
import com.wetube.wetube_service.dto.VideoDto;
import com.wetube.wetube_service.entity.Tag;
import com.wetube.wetube_service.entity.Video;
import com.wetube.wetube_service.entity.VideoTag;
import com.wetube.wetube_service.mapper.VideoMapper;
import com.wetube.wetube_service.service.CloudinaryService;
import com.wetube.wetube_service.service.VideoService;

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
    public VideoDto addTags(String videoId, String hashtagText) {
        UUID id = UUID.fromString(videoId);
        Video video = videoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Video not found with id: " + videoId));

        Set<String> names = parseHashtagText(hashtagText);

        for (String name : names) {
            Tag tag = tagRepository.findByNameIgnoreCase(name).orElseGet(() -> {
                Tag t = new Tag();
                t.setName(name);
                t.setCount(0);
                t.setCreatedAt(LocalDateTime.now());
                return tagRepository.save(t); // không saveAndFlush
            });

            if (!videoTagRepository.existsByVideo_IdAndTag_Id(video.getId(), tag.getId())) {
                VideoTag vt = new VideoTag();
                vt.setVideo(video);
                vt.setTag(tag);
                videoTagRepository.save(vt);

                // để object 'video' đang cầm có liên kết ngay:
                video.getVideoTags().add(vt);

                // tăng count (hoặc dùng tagRepository.incrementCount(tag.getId()))
                tag.setCount((tag.getCount() == null ? 0 : tag.getCount()) + 1);
            }
        }

        // map từ entity đã fetch‑join để tránh lazy-init/CME
        Video dtoSource = videoRepository.findByIdWithTags(id)
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
}
