package com.wetube.wetube_service.controller;

import java.util.List;
import java.util.UUID;

import com.wetube.wetube_service.dto.response.SubscribedChannelResponseDto;
import com.wetube.wetube_service.service.SubscriptionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/subscriptions")
@AllArgsConstructor
public class SubscriptionController {
    private final SubscriptionService subscriptionService;
    @Operation(
        summary = "Get subscribed channels by userId",
        description = "Retrieves the list of channels that the specified user is subscribed to"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Subscribed channels retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "User not found or no subscriptions available"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{userId}")
    public ResponseEntity<List<SubscribedChannelResponseDto>> getSubscribedChannels(@PathVariable UUID userId) {
        return ResponseEntity.ok(subscriptionService.getSubscribedChannels(userId));
    }
}
