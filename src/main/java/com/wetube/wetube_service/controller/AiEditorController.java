package com.wetube.wetube_service.controller;

import com.wetube.wetube_service.service.AI.AiEditorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/ai-editor")
public class AiEditorController {

    private final AiEditorService aiEditorService;

    public AiEditorController(AiEditorService aiEditorService) {
        this.aiEditorService = aiEditorService;
    }

    @PostMapping("/instructions")
    public ResponseEntity<Map<String, String>> getInstructions(@RequestBody Map<String, String> body) {
        String style = body.get("style");
        log.info("[Controller] Request instructions: style={}", style);

        try {
            String aiInstructions = aiEditorService.generateInstructions(style);
            return ResponseEntity.ok(Map.of("instructions", aiInstructions));
        } catch (Exception e) {
            log.error("[Controller] Failed to generate instructions", e);
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }


    @PostMapping("/process")
    public ResponseEntity<Map<String, String>> processVideo(
            @RequestParam("file") MultipartFile file,
            @RequestParam("instructions") String instructions) {
        log.info("[Controller] Received process request: file={}, instructions length={}", file.getOriginalFilename(), instructions.length());

        try {
            String videoUrl = aiEditorService.processVideoWithInstructions(file, instructions);
            log.info("[Controller] Process completed. Video URL={}", videoUrl);

            return ResponseEntity.ok(Map.of("videoUrl", videoUrl));
        } catch (Exception e) {
            log.error("[Controller] Processing failed", e);
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }
}
