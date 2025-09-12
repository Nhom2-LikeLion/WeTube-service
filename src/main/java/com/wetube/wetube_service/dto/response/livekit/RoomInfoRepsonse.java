package com.wetube.wetube_service.dto.response.livekit;

import lombok.Data;

@Data
public class RoomInfoRepsonse {
    private String roomId;
    private String name;
    private int numParticipants;
}
