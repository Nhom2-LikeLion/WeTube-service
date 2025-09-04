package com.wetube.wetube_service.service;

import com.wetube.wetube_service.dto.LikeDto;
import com.wetube.wetube_service.entity.Like;

import java.util.UUID;

public interface LikeService {

    LikeDto getLikeInfo(UUID targetId, Like.TargetType targetType, UUID userId);

    LikeDto toggleLike(UUID targetId, Like.TargetType targetType, UUID userId);
}
