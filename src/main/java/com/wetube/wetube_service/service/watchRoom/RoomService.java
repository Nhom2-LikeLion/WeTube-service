package com.wetube.wetube_service.service.watchRoom;

import org.springframework.stereotype.Service;

import java.util.*;

@Service("watchRoomService")
public class RoomService {
    private final Map<String, Set<String>> rooms = new HashMap<>();

    public void userJoin(String roomId, String username) {
        rooms.computeIfAbsent(roomId, k -> new HashSet<>()).add(username);
    }

    public void userLeave(String roomId, String username) {
        Set<String> users = rooms.get(roomId);
        if(users != null) {
            users.remove(username);
            if(users.isEmpty()) rooms.remove(roomId);
        }
    }

    public List<String> getUsers(String roomId) {
        return new ArrayList<>(rooms.getOrDefault(roomId, Set.of()));
    }
}

