package com.wetube.wetube_service.dto.post;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostDto {
    private Long id;
    private Long userId;
    private String content;
    private String imageUrl;
    private Integer likeCount;
    private Integer dislikeCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

