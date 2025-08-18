package com.wetube.wetube_service.dto.post;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PollSummaryDto {
//    private UUID postId;
    private List<PollOptionDto> options;
    private Integer totalVotes;
    private UUID userVotedOptionId;
}
