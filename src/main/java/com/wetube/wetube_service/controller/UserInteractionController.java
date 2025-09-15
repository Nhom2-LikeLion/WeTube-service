package com.wetube.wetube_service.controller;

import java.util.UUID;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wetube.wetube_service.enumeration.InteractionType;
import com.wetube.wetube_service.service.interaction.impl.UserInteractionServiceImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/interactions")
public class UserInteractionController {
    private final UserInteractionServiceImpl interactionService;

@PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> saveInteraction(
            @RequestParam("userId") UUID userId,
            @RequestParam("videoId") UUID videoId,
            @RequestParam("type") InteractionType type) {
        if (userId == null || videoId == null || type == null) {
            return ResponseEntity.badRequest().body("Missing parameters!");
        }
        interactionService.saveInteraction(userId, videoId, type);
        return ResponseEntity.ok("Interaction saved successfully.");
    }
}
