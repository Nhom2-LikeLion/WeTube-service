package com.wetube.wetube_service.controller;

import com.wetube.wetube_service.enumeration.InteractionType;
import com.wetube.wetube_service.service.UserInteractionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/interactions")
@RequiredArgsConstructor
public class UserInteractionController {

    private final UserInteractionService interactionService;

   @PostMapping
public ResponseEntity<?> saveInteraction(
        @RequestParam UUID userId,
        @RequestParam UUID videoId,
        @RequestParam InteractionType type
) {
    try {
        interactionService.saveInteraction(userId, videoId, type);
        return ResponseEntity.ok("Interaction saved with type=" + type);
    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(500).body("Error: " + e.getMessage());
    }
}

}
