package com.wetube.wetube_service.service.video.impl;

import com.wetube.wetube_service.dto.video.RecommendVideoDto;
import com.wetube.wetube_service.entity.video.UserTag;
import com.wetube.wetube_service.mapper.video.VideoMapper;
import com.wetube.wetube_service.repository.video.TagRepository;
import com.wetube.wetube_service.repository.video.UserTagRepository;
import com.wetube.wetube_service.search.VideoDocument;
import com.wetube.wetube_service.service.video.RecService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecServiceImpl implements RecService {

    private final UserTagRepository userTagRepo;
    private final TagRepository tagRepo;
    private final ElasticsearchOperations elasticOps;
    private final VideoMapper videoMapper;

    @Override
    public List<RecommendVideoDto> recommendVideos(UUID userId, int limit) {
        limit = Math.max(1, Math.min(50, limit));

        // lấy toàn bộ affinity
        List<UserTag> affinities = userTagRepo.findAllByUserIdOrderByPointDesc(userId);
        if (affinities.isEmpty()) {
            log.info("User {} chưa có tag nào → không recommend được", userId);
            return List.of();
        }

        // resolve tagId -> tagName
        List<String> userTags = affinities.stream()
                .map(ut -> tagRepo.findById(ut.getTagId())
                        .map(t -> t.getName())
                        .orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (userTags.isEmpty()) {
            log.info("User {} có UserTag nhưng không resolve được tagName", userId);
            return List.of();
        }

        log.debug("User {} có tag affinities: {}", userId, userTags);

        // xây criteria query thay cho QueryBuilders
        Criteria criteria = new Criteria("tags").in(userTags)
                .or(new Criteria("title").matches(String.join(" ", userTags)))
                .or(new Criteria("description").matches(String.join(" ", userTags)));

        CriteriaQuery query = new CriteriaQuery(criteria);
        query.setMaxResults(limit * 3);
        query.addSort(Sort.by(Sort.Order.desc("_score")));
        query.addSort(Sort.by(Sort.Order.desc("createdAt")));

        SearchHits<VideoDocument> hits = elasticOps.search(query, VideoDocument.class);
        if (hits.isEmpty()) {
            log.info("Không tìm thấy video phù hợp trong Elasticsearch cho user {}", userId);
            return List.of();
        }

        List<VideoDocument> docs = hits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());

        // tính điểm bổ sung
        double maxTagSum = 1.0;
        Map<String, Double> videoTagSum = new HashMap<>();
        for (VideoDocument v : docs) {
            double sum = 0;
            if (v.getTags() != null) {
                for (String t : v.getTags()) {
                    if (userTags.contains(t))
                        sum += 1;
                }
            }
            videoTagSum.put(v.getId(), sum);
            if (sum > maxTagSum)
                maxTagSum = sum;
        }

        record Scored(VideoDocument v, double score) {
        }
        List<Scored> scored = new ArrayList<>();
        for (VideoDocument v : docs) {
            double tagAffinity = videoTagSum.getOrDefault(v.getId(), 0.0) / maxTagSum;

            double freshness = 0.5;
            if (v.getCreatedAt() != null) {
                long ageDays = Duration.between(v.getCreatedAt(), Instant.now()).toDays();

                freshness = Math.exp(-0.08 * Math.max(0, ageDays));
            }

            double score = 0.70 * tagAffinity + 0.30 * freshness;
            scored.add(new Scored(v, score));
        }

        // sort giảm dần
        scored.sort((a, b) -> Double.compare(b.score, a.score));

        List<VideoDocument> top = scored.stream()
                .limit(limit)
                .map(Scored::v)
                .toList();

        return videoMapper.toRecommendDtoListFromDoc(top);
    }
}
