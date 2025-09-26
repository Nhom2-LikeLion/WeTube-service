package com.wetube.wetube_service.controller;

import com.wetube.wetube_service.dto.ai.TranscriptRequest;
import com.wetube.wetube_service.service.AI.AIService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/ai")
public class AIController {

    private final AIService aiService;

    public AIController(AIService aiService) {
        this.aiService = aiService;
    }

    @PostMapping("/description")
    public Map<String, String> generateDescription(@RequestBody Map<String, String> body) {
        String title = body.get("title");
        String tags = body.get("tags");
        String description = aiService.generateVideoDescription(title, tags);
        return Map.of("description", description);
    }

    @PostMapping("/transcript")
    public ResponseEntity<?> generateTranscript(@RequestBody TranscriptRequest request) {
        String transcript = aiService.generateTranscript(request.getVideoId(), request.getLang());
        return ResponseEntity.ok(Map.of("transcript", transcript));
    }
}
