package com.wetube.wetube_service.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wetube.wetube_service.dto.response.ChannelResponseDto;
import com.wetube.wetube_service.service.ChannelService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/channels")
@RequiredArgsConstructor
public class ChannelController {
    private final ChannelService channelService;

    @Operation(summary = "Get channel ", description = "Returns the list of channel ")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved channel"),
        @ApiResponse(responseCode = "500", description = "Server error, no specific details provided")
    })

    @GetMapping("/{channelId}")
    public ResponseEntity<ChannelResponseDto> getChannelById(@PathVariable UUID channelId) {
        ChannelResponseDto response = channelService.getChannelById(channelId);
        return ResponseEntity.ok(response);
    }
}
