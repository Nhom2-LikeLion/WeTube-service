package com.wetube.wetube_service.service.AI;

import com.wetube.wetube_service.service.CloudinaryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.nio.file.Files;

@Slf4j
@Service
public class AiEditorService {

    private final CloudinaryService cloudinaryService;
    private final GeminiClient geminiClient;

    public AiEditorService(CloudinaryService cloudinaryService, GeminiClient geminiClient) {
        this.cloudinaryService = cloudinaryService;
        this.geminiClient = geminiClient;
    }

    public String generateInstructions(String style) {
        try {
            log.info("[Service] Generating AI instructions for style={}", style);
            String aiInstructions = geminiClient.generateVideoInstructions(style);
            log.info("[Service] AI returned instructions={}", aiInstructions);
            return aiInstructions;
        } catch (Exception e) {
            log.error("[Service] Failed to generate instructions from Gemini", e);
            throw new RuntimeException("Failed to generate instructions", e);
        }
    }

    public String processVideoWithInstructions(MultipartFile file, String instructions) {
        File tempFile = null;
        File processedFile = null;

        try {
            tempFile = File.createTempFile("upload-", ".mp4");
            log.info("[Service] Step 1: Saving temp file {}", tempFile.getAbsolutePath());
            file.transferTo(tempFile);

            log.info("[Service] Step 2: Running FFmpeg with instructions={}", instructions);
            processedFile = FFmpegHelper.applyInstructions(tempFile, instructions);
            log.info("[Service] FFmpeg finished. Processed file at {}", processedFile.getAbsolutePath());

            log.info("[Service] Step 3: Uploading processed file to Cloudinary...");
            MultipartFile multipartProcessedFile = new MockMultipartFile(
                    "file",
                    processedFile.getName(),
                    "video/mp4",
                    Files.readAllBytes(processedFile.toPath())
            );

            String videoUrl = cloudinaryService.uploadVideo(multipartProcessedFile);
            log.info("[Service] Upload successful. Video URL={}", videoUrl);

            return videoUrl;
        } catch (Exception e) {
            log.error("[Service] Error during video processing", e);
            throw new RuntimeException("Video processing failed", e);
        } finally {
            log.info("[Service] Cleaning up temp files...");
            if (tempFile != null && tempFile.exists() && tempFile.delete()) {
                log.info("[Service] Deleted temp file {}", tempFile.getName());
            }
            if (processedFile != null && processedFile.exists() && processedFile.delete()) {
                log.info("[Service] Deleted processed file {}", processedFile.getName());
            }
        }
    }
}
