package com.wetube.wetube_service.service.impl;

import java.time.LocalDateTime;
import java.util.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wetube.wetube_service.Repository.UserTagRepository;
import com.wetube.wetube_service.Repository.VideoRepository;
import com.wetube.wetube_service.Repository.VideoTagRepository;
import com.wetube.wetube_service.dto.VideoDto;
import com.wetube.wetube_service.entity.UserTag;
import com.wetube.wetube_service.entity.Video;
import com.wetube.wetube_service.entity.VideoTag;
import com.wetube.wetube_service.mapper.VideoMapper;
import com.wetube.wetube_service.service.RecService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecServiceImpl implements RecService {

    private final VideoRepository videoRepo;
    private final VideoTagRepository videoTagRepo;
    private final UserTagRepository userTagRepo;
    private final VideoMapper videoMapper;

    @Override
    @Transactional(readOnly = true)
    public List<VideoDto> recommendVideos(UUID userId, int limit) {
        limit = Math.max(1, Math.min(50, limit));

        List<UserTag> affinities = userTagRepo.findAllByUserIdOrderByPointDesc(userId);
        if (affinities.isEmpty()) {
            List<Video> latest = videoRepo.findAllByVideosStatusOrderByCreatedAtDesc("public");
            return videoMapper.toDtoList(latest.stream().limit(limit).toList());
        }

        Map<UUID, Double> tag2point = new HashMap<>();
        for (UserTag ut : affinities) tag2point.put(ut.getTagId(), ut.getPoint());

        List<Video> candidates = videoRepo.findAllByVideosStatusOrderByCreatedAtDesc("public");

        double maxTagSum = 1.0;
        Map<UUID, Double> videoTagSum = new HashMap<>();
        for (Video v : candidates) {
            double sum = 0;
            for (VideoTag vt : videoTagRepo.findByVideo_Id(v.getId())) {
                sum += tag2point.getOrDefault(vt.getTag().getId(), 0.0);
            }
            videoTagSum.put(v.getId(), sum);
            if (sum > maxTagSum) maxTagSum = sum;
        }

        record Scored(Video v, double score) {}
        List<Scored> scored = new ArrayList<>();
        for (Video v : candidates) {
            double tagAffinity = videoTagSum.get(v.getId()) / maxTagSum; // [0..1]

            long ageDays = java.time.Duration.between(
                    v.getCreatedAt(), LocalDateTime.now()).toDays();
            double freshness = Math.exp(-0.08 * Math.max(0, ageDays)); // [0..1]

            double score = 0.70 * tagAffinity + 0.30 * freshness;
            scored.add(new Scored(v, score));
        }

        scored.sort((a, b) -> Double.compare(b.score, a.score));
        List<Video> top = scored.stream().limit(limit).map(Scored::v).toList();

        return videoMapper.toDtoList(top);
    }
}
