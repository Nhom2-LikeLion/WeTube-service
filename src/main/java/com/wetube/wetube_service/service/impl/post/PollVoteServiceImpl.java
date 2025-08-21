package com.wetube.wetube_service.service.impl.post;

import com.wetube.wetube_service.dto.post.PollSummaryDto;
import com.wetube.wetube_service.dto.post.VoteRequestDto;
import com.wetube.wetube_service.entity.post.PollOption;
import com.wetube.wetube_service.entity.post.PollVote;
import com.wetube.wetube_service.repository.post.PollOptionRepository;
import com.wetube.wetube_service.repository.post.PollVoteRepository;
import com.wetube.wetube_service.service.post.PostPollService;
import com.wetube.wetube_service.service.post.PollVoteService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PollVoteServiceImpl implements PollVoteService {

    private final PollVoteRepository pollVoteRepository;
    private final PollOptionRepository pollOptionRepository;
    private final PostPollService postPollService;

    @Override
    @Transactional
    public PollSummaryDto vote(VoteRequestDto request) {
        PollOption option = pollOptionRepository.findById(request.getOptionId())
                .orElseThrow(() -> new RuntimeException("Option not found"));

        if (!option.getPost().getId().equals(request.getPostId())) {
            throw new RuntimeException("Option does not belong to the given post");
        }

        pollVoteRepository.deleteByPostIdAndUserId(request.getPostId(), request.getUserId());

        PollVote v = PollVote.builder() //newvote
                .postId(request.getPostId())
                .optionId(request.getOptionId())
                .userId(request.getUserId())
                .build();
        pollVoteRepository.save(v);

        return postPollService.getPollSummary(request.getPostId(), request.getUserId());
    }
}
