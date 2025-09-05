package com.wetube.wetube_service.service;

import java.util.List;
import java.util.UUID;

import com.wetube.wetube_service.dto.VideoDto;
import com.wetube.wetube_service.dto.response.RecResponse;

public interface RecService {
    RecResponse recommendVideos (UUID userId, int limit);
}
