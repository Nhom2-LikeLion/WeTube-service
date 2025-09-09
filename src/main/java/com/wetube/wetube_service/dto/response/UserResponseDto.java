package com.wetube.wetube_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDto {
    private String email;
    private String name;
    private String avatarUrl;
    private LocalDateTime createdAt;
    private UserChannelResponseDto channel;
}
