package com.wetube.wetube_service.service.video;

import java.util.List;
import java.util.UUID;

import com.wetube.wetube_service.dto.video.RecommendVideoDto;

public interface RecService {
    List<RecommendVideoDto> recommendVideos (UUID userId, int limit);
}
