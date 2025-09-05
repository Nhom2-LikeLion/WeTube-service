package com.wetube.wetube_service.service.impl;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.wetube.wetube_service.dto.VideoDto;
import com.wetube.wetube_service.entity.playlist.Video;
import com.wetube.wetube_service.mapper.PlaylistMapper;
import com.wetube.wetube_service.repository.VideoRepository;
import com.wetube.wetube_service.service.Videoservice;

public class VideoServiceImpl implements Videoservice {
     private final VideoRepository videoRepository;

    public VideoServiceImpl(VideoRepository videoRepository) {
        this.videoRepository = videoRepository;
    }

    @Override
    public VideoDto getVideoById(UUID id) {
        Video video = videoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Video not found"));
        return PlaylistMapper.toVideoDto(video);
    }

    @Override
    public List<VideoDto> getAllVideos() {
        return videoRepository.findAll()
                .stream()
                .map(PlaylistMapper:: toVideoDto)
                .collect(Collectors.toList());
    }
}
