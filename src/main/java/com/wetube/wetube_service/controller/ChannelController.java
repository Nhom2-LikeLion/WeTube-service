package com.wetube.wetube_service.controller;

import com.wetube.wetube_service.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/channels")
@RequiredArgsConstructor
public class ChannelController {
    private final ChannelService channelService;

    @PostMapping("/init/{userId}/{ip}")
    public ResponseEntity<Void> initiateChannel(@PathVariable UUID userId, @PathVariable String ip) {
        channelService.initiateChannel(userId,ip);
        return ResponseEntity.ok().build();
    }
}
