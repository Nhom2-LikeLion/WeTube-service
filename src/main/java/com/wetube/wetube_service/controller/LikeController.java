package com.wetube.wetube_service.controller;

import com.wetube.wetube_service.dto.LikeDto;
import com.wetube.wetube_service.entity.Like;
import com.wetube.wetube_service.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/likes")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @GetMapping
    public ResponseEntity<LikeDto> getLikeInfo(
            @RequestParam UUID targetId,
            @RequestParam Like.TargetType targetType,
            @RequestParam(required = false) UUID userId
    ) {
        return ResponseEntity.ok(likeService.getLikeInfo(targetId, targetType, userId));
    }

    @PostMapping("/toggle")
    public ResponseEntity<LikeDto> toggleLike(
            @RequestParam UUID targetId,
            @RequestParam Like.TargetType targetType,
            @RequestParam UUID userId
    ) {
        return ResponseEntity.ok(likeService.toggleLike(targetId, targetType, userId));
    }
}
