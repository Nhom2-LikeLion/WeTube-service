package com.wetube.wetube_service.dto.response.livekit;

import lombok.Data;

@Data
public class JoinStreamResponse {
    private String auth_token;
    private ConnectionDetails connection_details;
}