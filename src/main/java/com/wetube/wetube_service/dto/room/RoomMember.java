package com.wetube.wetube_service.dto.room;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RoomMember {
    private int count;
    private List<RoomMember> members;
    private String username;
}
