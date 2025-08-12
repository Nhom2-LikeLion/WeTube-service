package com.wetube.wetube_service.dto;

import com.wetube.wetube_service.entity.Comment;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDto {
    private UUID targetId;
    private UUID userId;
    private String content;
    private Integer likeCount;
    private Integer dislikeCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Comment.TargetType targetType;
}

