package com.wetube.wetube_service.dto.video;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoDetailResponseDto {
    private VideoDetailDto detail;
    private RecommendResponseDto recommend;
}

