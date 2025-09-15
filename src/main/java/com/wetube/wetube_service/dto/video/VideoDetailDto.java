package com.wetube.wetube_service.dto.video;

import java.time.LocalDate;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoDetailDto {
    private UUID id;
    private String title;
    private String description;
    private String videoUrl;
    private LocalDate createAt;
    private Integer totalView;
    private String name;
    private String picture;
}
