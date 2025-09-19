package com.wetube.wetube_service.dto.video;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import com.wetube.wetube_service.dto.UserDto;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VideoDto {
    private UUID id;
//    private String usersId;
    private String title;
    private String description;
    private String thumbnailUrl;
    private String videoUrl;
    private String videosStatus;
    private float duration;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private String tagsAsString;
    private Set<TagDto> tags;
    private UserDto user;
}
