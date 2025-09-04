package com.wetube.wetube_service.service;

import java.util.List;
import java.util.UUID;

import com.wetube.wetube_service.dto.VideoDto;

public interface RecService {
    List<VideoDto> recommendVideos (UUID userId, int limit);
}
