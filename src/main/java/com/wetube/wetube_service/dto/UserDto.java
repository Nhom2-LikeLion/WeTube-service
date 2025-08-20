package com.wetube.wetube_service.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private String clerkId;
    private String email;
    private String name;
    private String avatarUrl;
}
