package com.wetube.wetube_service.service.AI;

import com.wetube.wetube_service.dto.AIEditor.AiEditorRequestDto;
import com.wetube.wetube_service.dto.AIEditor.AiEditorResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AiEditorService {

    private final RestTemplate restTemplate;

    public AiEditorService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public AiEditorResponseDto editVideo(String videoUrl, String style) {
        AiEditorRequestDto request = new AiEditorRequestDto();
        request.setVideoUrl(videoUrl);
        request.setStyle(style);

        String aiServiceUrl = "http://localhost:5000/ai-edit";

        return restTemplate.postForObject(aiServiceUrl, request, AiEditorResponseDto.class);
    }
}

