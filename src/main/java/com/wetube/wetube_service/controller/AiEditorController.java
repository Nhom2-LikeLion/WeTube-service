package com.wetube.wetube_service.controller;

import com.wetube.wetube_service.dto.AIEditor.AiEditorRequestDto;
import com.wetube.wetube_service.dto.AIEditor.AiEditorResponseDto;
import com.wetube.wetube_service.service.AI.AiEditorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/videos")
public class AiEditorController {

    private final AiEditorService aiEditorService;

    public AiEditorController(AiEditorService aiEditorService) {
        this.aiEditorService = aiEditorService;
    }

    @PostMapping("/ai-edit")
    public ResponseEntity<?> aiEditVideo(@RequestBody AiEditorRequestDto request) {
        AiEditorResponseDto response = aiEditorService.editVideo(request.getVideoUrl(), request.getStyle());
        return ResponseEntity.ok(response);
    }
}

