package com.wetube.wetube_service.dto;

import java.util.UUID;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VideoDto {
    private UUID id;
    private String title;
    private String videoUrl;
    private String thumbnailUrl;
    private float duration;
}
