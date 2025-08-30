package com.wetube.wetube_service.service.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.wetube.wetube_service.Repository.UserTagRepository;
import com.wetube.wetube_service.Repository.VideoRepository;
import com.wetube.wetube_service.Repository.VideoTagRepository;
import com.wetube.wetube_service.entity.UserTag;
import com.wetube.wetube_service.entity.VideoTag;
import com.wetube.wetube_service.enumeration.InteractionType;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserInteractionServiceImpl {
     
    private final VideoRepository videoRepo;
    private final VideoTagRepository videoTagRepo;
    private final UserTagRepository userTagRepo;

    @Transactional
    public void saveInteraction(UUID userId, UUID videoId, InteractionType type) {

        if (!videoRepo.existsById(videoId)) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.NOT_FOUND, "Video not found");
        }

        double delta = switch (type) {
            case VIEW -> 1.0;
            case LIKE -> 5.0;
            case COMMENT -> 8.0;
            case SHARE -> 12.0;
            default -> 0.0;
        };

        for (VideoTag vt : videoTagRepo.findByVideo_Id(videoId)) {
            UUID tagId = vt.getTag().getId();
            var pk = new UserTag.UserTagId(userId, tagId);
            var existing = userTagRepo.findById(pk).orElse(null);

            if (existing == null) {
                try {
                    userTagRepo.save(UserTag.builder()
                            .userId(userId)
                            .tagId(tagId)
                            .point(delta)
                            .build());
                } catch (org.springframework.dao.DataIntegrityViolationException e) {
                    var again = userTagRepo.findById(pk).orElseThrow();
                    again.setPoint(Math.min(again.getPoint() + delta, 1000));
                    userTagRepo.save(again);
                }
            } else {
                existing.setPoint(Math.min(existing.getPoint() + delta, 1000));
                userTagRepo.save(existing);
            }
        }
    }
}
