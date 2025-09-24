package com.wetube.wetube_service.controller;


import com.wetube.wetube_service.dto.room.JoinRoomRequest;
import com.wetube.wetube_service.dto.room.Room;
import com.wetube.wetube_service.dto.room.WatchMember;
import com.wetube.wetube_service.service.watchRoom.RoomService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.MessageHeaders;
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
    public void handleCreateRoom(@Payload String username,
                                 SimpMessageHeaderAccessor headerAccessor) {
        System.out.println("Received username: " + username);

        WatchMember host = new WatchMember();
        host.setUsername(username);
        host.setHost(true);

        Room room = roomService.createRoom(host);

        String sessionId = headerAccessor.getSessionId();

        // Gửi trực tiếp cho người tạo phòng
        messagingTemplate.convertAndSendToUser(
                sessionId,
                "/queue/room/created",
                room,
                createHeaders(sessionId)
        );
    }


    @MessageMapping("/room/join")
    @SendTo("/topic/room.{roomId}.members")
    public void handleJoinRoom(@Payload JoinRoomRequest request,
                               SimpMessageHeaderAccessor headerAccessor) {
        Room updatedRoom = roomService.addMember(request.getRoomId(), request.getUsername());

        // Gửi thông tin room cập nhật cho tất cả client trong topic
        messagingTemplate.convertAndSend("/topic/room/" + request.getRoomId() + "/members", updatedRoom);
    }

    private MessageHeaders createHeaders(String sessionId) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId(sessionId);
        headerAccessor.setLeaveMutable(true);
        return headerAccessor.getMessageHeaders();
    }

}

