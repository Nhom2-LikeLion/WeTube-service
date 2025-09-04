package com.wetube.wetube_service.service.impl;

import com.wetube.wetube_service.dto.LikeDto;
import com.wetube.wetube_service.entity.Like;
import com.wetube.wetube_service.repository.LikeRepository;
import com.wetube.wetube_service.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

    private final LikeRepository likeRepository;

    @Override
    public LikeDto getLikeInfo(UUID targetId, Like.TargetType targetType, UUID userId) {
        int likeCount = likeRepository.countByTargetTypeAndTargetId(targetType, targetId);
        List<UUID> likedUserIds = likeRepository.findByTargetTypeAndTargetId(targetType, targetId)
                .stream()
                .map(Like::getUserId)
                .collect(Collectors.toList());

        Boolean liked = null;
        if (userId != null) {
            liked = likeRepository.existsByTargetTypeAndTargetIdAndUserId(targetType, targetId, userId);
        }

        return LikeDto.builder()
                .targetId(targetId)
                .targetType(targetType)
                .likeCount(likeCount)
                .likedUserIds(likedUserIds)
                .liked(liked)
                .build();
    }

    @Override
    public LikeDto toggleLike(UUID targetId, Like.TargetType targetType, UUID userId) {
        boolean alreadyLiked = likeRepository.existsByTargetTypeAndTargetIdAndUserId(targetType, targetId, userId);

        if (alreadyLiked) {
            likeRepository.deleteAll(
                    likeRepository.findByTargetTypeAndTargetId(targetType, targetId)
                            .stream()
                            .filter(like -> like.getUserId().equals(userId))
                            .toList()
            );
        } else {
            Like like = Like.builder()
                    .targetId(targetId)
                    .targetType(targetType)
                    .userId(userId)
                    .build();
            likeRepository.save(like);
        }

        int likeCount = likeRepository.countByTargetTypeAndTargetId(targetType, targetId);
        List<UUID> likedUserIds = likeRepository.findByTargetTypeAndTargetId(targetType, targetId)
                .stream()
                .map(Like::getUserId)
                .toList();

        boolean isNowLiked = likeRepository.existsByTargetTypeAndTargetIdAndUserId(targetType, targetId, userId);

        return LikeDto.builder()
                .targetId(targetId)
                .targetType(targetType)
                .likeCount(likeCount)
                .likedUserIds(likedUserIds)
                .liked(isNowLiked)
                .build();
    }
}

