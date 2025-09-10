package com.wetube.wetube_service.controller;

import com.wetube.wetube_service.dto.RoomDto;
import com.wetube.wetube_service.entity.Room;
import com.wetube.wetube_service.mapper.RoomMapper;
import com.wetube.wetube_service.service.RoomService;
import io.livekit.server.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomService roomService;

    @Value("${livekit.apiKey}")
    private String livekitApiKey;

    @Value("${livekit.apiSecret}")
    private String livekitApiSecret;

    @Value("${livekit.url}")
    private String livekitUrl;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping("/create")
    public ResponseEntity<RoomDto> createRoom(
            @RequestParam String roomId,
            @RequestParam String roomName,
            @RequestParam String userId
    ) {
        Room room = roomService.createRoom(roomId, roomName, userId);
        return ResponseEntity.ok(RoomMapper.toDto(room));
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<RoomDto> getRoom(@PathVariable String roomId) {
        return roomService.getRoomByRoomId(roomId)
                .map(room -> ResponseEntity.ok(RoomMapper.toDto(room)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{roomId}/join")
    public ResponseEntity<?> joinRoom(
            @PathVariable String roomId,
            @RequestBody Map<String, String> body
    ) {
        String userId = body.get("userId");
        String username = body.getOrDefault("username", "Guest");

        Optional<Room> roomOpt = roomService.getRoomByRoomId(roomId);
        if (roomOpt.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("error", "Room not found"));
        }

        try {
            AccessToken token = new AccessToken(livekitApiKey, livekitApiSecret);
            token.setIdentity(userId);
            token.setName(username);
            token.addGrants(
                    new RoomJoin(true),
                    new RoomName(roomId)
            );

            String jwt = token.toJwt();

            return ResponseEntity.ok(Map.of(
                    "token", jwt,
                    "room", RoomMapper.toDto(roomOpt.get()),
                    "livekitUrl", livekitUrl
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/list")
    public ResponseEntity<List<RoomDto>> listRooms() {
        List<Room> rooms = roomService.listRooms();
        List<RoomDto> dtoList = rooms.stream()
                .map(RoomMapper::toDto)
                .toList();
        return ResponseEntity.ok(dtoList);
    }
}
