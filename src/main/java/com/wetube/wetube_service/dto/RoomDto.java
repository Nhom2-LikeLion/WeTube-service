package com.wetube.wetube_service.dto;

import lombok.AllArgsConstructor;
import lombok.*;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomDto {
    private String roomId;
    private String roomName;
    private String userId;
    private LocalDateTime createdAt;
}
