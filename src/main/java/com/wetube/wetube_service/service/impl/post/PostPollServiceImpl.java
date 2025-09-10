package com.wetube.wetube_service.service.impl.post;

import com.wetube.wetube_service.dto.post.PollOptionDto;
import com.wetube.wetube_service.dto.post.PollSummaryDto;
import com.wetube.wetube_service.entity.post.PollOption;
import com.wetube.wetube_service.entity.post.PollVote;
import com.wetube.wetube_service.repository.post.PollOptionRepository;
import com.wetube.wetube_service.repository.post.PollVoteRepository;
import com.wetube.wetube_service.service.post.PostPollService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class PostPollServiceImpl implements PostPollService {

    private final PollOptionRepository pollOptionRepository;
    private final PollVoteRepository pollVoteRepository;

    @Override
    public PollSummaryDto getPollSummary(UUID postId, UUID userIdOrNull) {
        List<PollOption> options = pollOptionRepository.findByPostId(postId);
        int total = pollVoteRepository.countByPostId(postId);
        UUID userVotedOptionId = null;

        if (userIdOrNull != null) {
            userVotedOptionId = pollVoteRepository.findByPostIdAndUserId(postId, userIdOrNull)
                    .map(PollVote::getOptionId)
                    .orElse(null);
        }

        List<PollOptionDto> optionDtos = options.stream().map(opt -> {
            int votes = pollVoteRepository.countByPostIdAndOptionId(postId, opt.getId());
            double pct = total == 0 ? 0.0 : (votes * 100.0 / total);
            return PollOptionDto.builder()
                    .optionId(opt.getId())
                    .optionText(opt.getOptionText())
                    .voteCount(votes)
                    .percentage(Math.round(pct * 10.0) / 10.0) // làm tròn 1 số lẻ
                    .build();
        }).toList();

        return PollSummaryDto.builder()
                .id(postId)
                .options(optionDtos)
                .totalVotes(total)
                .userVotedOptionId(userVotedOptionId)
                .build();
    }

    @Override
    @Transactional
    public void vote(UUID postId, UUID optionId, UUID userId) {
        pollVoteRepository.findByPostIdAndUserId(postId, userId).ifPresentOrElse(
                existing -> {
                    if (!existing.getOptionId().equals(optionId)) {
                        existing.setOptionId(optionId);
                        pollVoteRepository.save(existing);
                    }
                },
                () -> {
                    PollVote vote = PollVote.builder()
                            .postId(postId)
                            .optionId(optionId)
                            .userId(userId)
                            .build();
                    pollVoteRepository.save(vote);
                }
        );
    }
}