package com.wetube.wetube_service.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wetube.wetube_service.dto.response.SubscribedChannelResponseDto;
import com.wetube.wetube_service.service.SubscriptionService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/subscriptions")
@AllArgsConstructor
public class SubscriptionController {
private final  SubscriptionService subscriptionService;
    @GetMapping("/{userId}")
    public ResponseEntity<List<SubscribedChannelResponseDto>> getSubscribedChannels(@PathVariable UUID userId) {
        return ResponseEntity.ok(subscriptionService.getSubscribedChannels(userId));
    }
}
