package com.wetube.wetube_service.dto.response.livekit;

import lombok.Data;

@Data
public class ParticipantDto {
    private String identity;
    private String name;
    private String metadata;
    private boolean isPublisher;
    private boolean isMuted;
}
