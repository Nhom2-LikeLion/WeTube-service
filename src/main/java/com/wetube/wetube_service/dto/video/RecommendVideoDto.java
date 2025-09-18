package com.wetube.wetube_service.dto.video;

import java.time.LocalDate;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecommendVideoDto {
    private UUID id;
    private String title;
    private String thumbnailUrl;
    private Integer totalView;
    private LocalDate createAt;
    private String name;
    private Long duration;
    private String picture;
}
