package com.wetube.wetube_service.controller;


import com.wetube.wetube_service.dto.room.*;
import com.wetube.wetube_service.service.watchRoom.RoomService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@AllArgsConstructor
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomService roomService;
    private final SimpMessagingTemplate messagingTemplate;

    @GetMapping
    public ResponseEntity<Map<String, Room>> getAllRooms(){
        return ResponseEntity.ok(roomService.getRooms());
    }

    @MessageMapping("/room/create")
    @SendTo("/topic/room/create")
    public Room handleCreateRoom(@Payload String username) {
        System.out.println("Received username: " + username);
        WatchMember host = new WatchMember();
        host.setUsername(username);
        host.setHost(true);

        Room room = roomService.createRoom(host);
        return room;
    }

    @MessageMapping("/chat/{roomId}")
    @SendTo("/topic/rooms/chat/{roomId}")
    public ClientChatMessage handleChat(
            @DestinationVariable String roomId,
            @Payload ClientChatMessage message) {
        System.out.println("Received message in room " + roomId + ": " + message);
        return message;
    }

    @MessageMapping("/room/addSong/{roomId}")
    @SendTo("/topic/rooms/addSong/{roomId}")
    public VideoRoom handleAddSong(
            @DestinationVariable String roomId,
            @Payload String videoId) {
        System.out.println("Received Video Add in room " + roomId + ": " + videoId);
        return roomService.addSong(roomId,videoId);
    }
//    @MessageMapping("/room/join")
//    @SendTo("/topic/room.{roomId}.members")
//    public void handleJoinRoom(@Payload JoinRoomRequest request,
//                               SimpMessageHeaderAccessor headerAccessor) {
//        Room updatedRoom = roomService.addMember(request.getRoomId(), request.getUsername());
//
//        // Gửi thông tin room cập nhật cho tất cả client trong topic
//        messagingTemplate.convertAndSend("/topic/room/" + request.getRoomId() + "/members", updatedRoom);
//    }

}

