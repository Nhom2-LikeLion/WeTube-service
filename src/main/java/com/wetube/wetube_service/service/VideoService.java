package com.wetube.wetube_service.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.wetube.wetube_service.dto.VideoDto;

public interface VideoService {
    VideoDto createVideo(MultipartFile videoFile, MultipartFile thumbnailFile, VideoDto videoDto) throws Exception;
    VideoDto getById(String id);
    List<VideoDto> getAllVideo();
    VideoDto addTags(String videoId, String hashtagText);

}
