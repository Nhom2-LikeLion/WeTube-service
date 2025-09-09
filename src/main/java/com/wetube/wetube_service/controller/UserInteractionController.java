package com.wetube.wetube_service.controller;

import java.util.UUID;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wetube.wetube_service.enumeration.InteractionType;
import com.wetube.wetube_service.service.impl.UserInteractionServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/interactions")
public class UserInteractionController {
    private final UserInteractionServiceImpl interactionService;
    
@Operation(
    summary = "Save a user interaction with a video",
    description = "Saves an interaction (like, view, etc.) between a user and a video"
)
@ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Interaction saved successfully"),
    @ApiResponse(responseCode = "400", description = "Missing or invalid parameters"),
    @ApiResponse(responseCode = "500", description = "Internal server error")
})
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
