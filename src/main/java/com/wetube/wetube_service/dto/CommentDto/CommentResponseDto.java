package com.wetube.wetube_service.dto.CommentDto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResponseDto {
    private UUID id;
    private String content;
    private UUID userId;
    private Integer likeCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer replyCount;
    private List<CommentResponseDto> replies;
}
