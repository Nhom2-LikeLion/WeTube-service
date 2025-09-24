package com.wetube.wetube_service.dto.room;

import lombok.*;

@Getter
@Setter
public class ClientChatMessage {
    private String type;
    private String sender;
    private String content;

    @Override
    public String toString() {
        return "ClientChatMessage{" +
                "type='" + type + '\'' +
                ", sender='" + sender + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
