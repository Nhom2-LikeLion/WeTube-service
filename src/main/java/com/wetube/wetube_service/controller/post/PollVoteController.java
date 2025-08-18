package com.wetube.wetube_service.controller.post;

import com.wetube.wetube_service.dto.post.PollSummaryDto;
import com.wetube.wetube_service.dto.post.VoteRequestDto;
import com.wetube.wetube_service.service.post.PollVoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/votes")
@RequiredArgsConstructor
public class PollVoteController {

    private final PollVoteService pollVoteService;

    @PostMapping
    public ResponseEntity<PollSummaryDto> vote(@RequestBody VoteRequestDto request) {
        return ResponseEntity.ok(pollVoteService.vote(request));
    }
}

