package com.wetube.wetube_service.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wetube.wetube_service.dto.LikeDto;
import com.wetube.wetube_service.entity.Like;
import com.wetube.wetube_service.service.LikeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/likes")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;
    @Operation(summary = "Get like info", description = "Retrieves like information for a given target (post, comment, etc.) and optionally by userId")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Like info retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Target not found")
    })
    @GetMapping
    public ResponseEntity<LikeDto> getLikeInfo(
            @RequestParam UUID targetId,
            @RequestParam Like.TargetType targetType,
            @RequestParam(required = false) UUID userId
    ) {
        return ResponseEntity.ok(likeService.getLikeInfo(targetId, targetType, userId));
    }
    @Operation(summary = "Toggle like status", description = "Toggles like or unlike for a given target and userId")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Like status toggled successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input or target/user not found")
    })
    @PostMapping("/toggle")
    public ResponseEntity<Void> toggleLike(
            @RequestParam UUID targetId,
            @RequestParam Like.TargetType targetType,
            @RequestParam UUID userId
    ) {
        likeService.toggleLike(targetId, targetType, userId);
        return ResponseEntity.ok().build();
    }
}
