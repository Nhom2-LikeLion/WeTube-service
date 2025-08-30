package com.wetube.wetube_service.service;

import com.wetube.wetube_service.enumeration.InteractionType;

import jakarta.transaction.Transactional;

import com.wetube.wetube_service.Repository.UserTagRepository;
import com.wetube.wetube_service.Repository.VideoTagRepository;
import com.wetube.wetube_service.entity.UserTag;
import com.wetube.wetube_service.entity.VideoTag;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserInteractionService {

    private final UserTagRepository userTagRepo;
    private final VideoTagRepository videoTagRepo;

    @Transactional
    public void saveInteraction(UUID userId, UUID videoId, InteractionType type) {
        double delta = switch(type){
            case VIEW ->1.0;
            case LIKE ->5.0;
            case SHARE ->8.0;
            default -> 0.0;
        };

        List<VideoTag> videoTags = videoTagRepo.findByVideo_Id(videoId);

        for(VideoTag vt: videoTags) {
            UUID tagId = vt.getTag().getId();

            UserTag.UserTagId pk = new UserTag.UserTagId(userId, tagId);
            UserTag existing = userTagRepo.findById(pk).orElse(null);

            if(existing == null) {
                UserTag ut = UserTag.builder()
                .userId(userId)
                .tagId(tagId)
                .point(delta)
                .build();
            userTagRepo.save(ut);
            }else {
                existing.setPoint(Math.min(existing.getPoint() + delta, 1000));
                userTagRepo.save(existing);
            }
        }
    }
   }
