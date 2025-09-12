package com.wetube.wetube_service.mapper;

import com.wetube.wetube_service.dto.RoomDto;
import com.wetube.wetube_service.entity.Room;

public class RoomMapper {
    public static RoomDto toDto(Room room) {
        return new RoomDto(
                room.getRoomId(),
                room.getRoomName(),
                room.getUserId(),
                room.getCreatedAt()
        );
    }
}
