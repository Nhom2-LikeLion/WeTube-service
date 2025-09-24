package com.wetube.wetube_service.controller;

import com.wetube.wetube_service.dto.room.ClientChatMessage;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;

    public ChatController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/chat.{roomId}")
    public void handleChat(
            @DestinationVariable String roomId,
            @Payload ClientChatMessage message
    ) {
        System.out.println("Received message in room " + roomId + ": " + message);

        messagingTemplate.convertAndSend(
                "/topic/rooms.chat." + roomId,
                message
        );
    }
}
