package com.wetube.wetube_service.controller;

import com.wetube.wetube_service.dto.room.RoomMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class RoomController {

    private final SimpMessagingTemplate messagingTemplate;

    public RoomController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/room/{roomId}")
    public void sendMessage(@Payload RoomMessage message, @org.springframework.messaging.handler.annotation.DestinationVariable String roomId) {
        messagingTemplate.convertAndSend("/topic/room." + roomId, message);
    }
}
