package com.wetube.wetube_service.service;

import com.wetube.wetube_service.entity.Room;
import com.wetube.wetube_service.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoomService {
    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public Room createRoom(String roomId, String roomName, String userId) {
        Room room = new Room();
        room.setRoomId(roomId);
        room.setRoomName(roomName);
        room.setUserId(userId);
        return roomRepository.save(room);
    }

    public Optional<Room> getRoomByRoomId(String roomId) {
        return roomRepository.findByRoomId(roomId);
    }

    public List<Room> listRooms() {
        return roomRepository.findAll();
    }
}
