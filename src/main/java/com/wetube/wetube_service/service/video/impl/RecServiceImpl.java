package com.wetube.wetube_service.service.video.impl;

import java.time.LocalDateTime;
import java.util.*;

import com.wetube.wetube_service.dto.video.RecommendVideoDto;
import com.wetube.wetube_service.entity.video.UserTag;
import com.wetube.wetube_service.entity.video.Video;
import com.wetube.wetube_service.entity.video.VideoTag;
import com.wetube.wetube_service.enumeration.ActiveStatus;
import com.wetube.wetube_service.mapper.video.VideoMapper;
import com.wetube.wetube_service.repository.video.UserTagRepository;
import com.wetube.wetube_service.repository.video.VideoRepository;
import com.wetube.wetube_service.repository.video.VideoTagRepository;
import com.wetube.wetube_service.service.video.RecService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecServiceImpl implements RecService {

    private final VideoRepository videoRepo;
    private final VideoTagRepository videoTagRepo;
    private final UserTagRepository userTagRepo;
    private final VideoMapper videoMapper;

    @Override
    @Transactional(readOnly = true)
    public List<RecommendVideoDto> recommendVideos(UUID userId, int limit) {
        limit = Math.max(1, Math.min(50, limit));

        // lấy toàn bộ affinity (userTag)
        List<UserTag> affinities = userTagRepo.findAllByUserIdOrderByPointDesc(userId);
        if (affinities.isEmpty()) {
            log.info("❌ User {} chưa có tag nào → không recommend được", userId);
            return List.of();
        }

        // map tagId -> point
        Map<UUID, Double> tag2point = new HashMap<>();
        for (UserTag ut : affinities) {
            tag2point.put(ut.getTagId(), ut.getPoint());
        }
        log.debug("🎯 User {} có tag affinities: {}", userId, tag2point);

        // lấy toàn bộ video ACTIVE (chỉnh lại enum cho khớp DB)
        List<Video> candidates = videoRepo.findAllByVideosStatusOrderByCreatedAtDesc(ActiveStatus.ACTIVE);
        if (candidates.isEmpty()) {
            log.info("❌ Không tìm thấy video ACTIVE nào để recommend");
            return List.of();
        }

        double maxTagSum = 1.0;
        Map<UUID, Double> videoTagSum = new HashMap<>();

        for (Video v : candidates) {
            double sum = 0;
            List<VideoTag> vtags = videoTagRepo.findByVideo_Id(v.getId());
            log.debug("📹 Video {} có tags: {}", v.getId(),
                    vtags.stream().map(vt -> vt.getTag().getName()).toList());

            for (VideoTag vt : vtags) {
                sum += tag2point.getOrDefault(vt.getTag().getId(), 0.0);
            }
            videoTagSum.put(v.getId(), sum);
            if (sum > maxTagSum) maxTagSum = sum;
        }

        record Scored(Video v, double score) {}
        List<Scored> scored = new ArrayList<>();

        for (Video v : candidates) {
            double tagAffinity = videoTagSum.getOrDefault(v.getId(), 0.0) / maxTagSum;

            long ageDays = java.time.Duration.between(v.getCreatedAt(), LocalDateTime.now()).toDays();
            double freshness = Math.exp(-0.08 * Math.max(0, ageDays));

            double score = 0.70 * tagAffinity + 0.30 * freshness;
            scored.add(new Scored(v, score));

            log.debug("➡️ Video {}: tagAffinity={}, freshness={}, score={}",
                    v.getId(), tagAffinity, freshness, score);
        }

        // sort giảm dần theo score
        scored.sort((a, b) -> Double.compare(b.score, a.score));

        // lấy top limit
        List<Video> top = scored.stream().limit(limit).map(Scored::v).toList();

        log.info("✅ Recommend cho user {}: {} video(s)", userId, top.size());
        return videoMapper.toRecommendDtoList(top);
    }
}
