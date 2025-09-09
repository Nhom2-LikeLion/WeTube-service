package com.wetube.wetube_service.controller;

import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.wetube.wetube_service.dto.VideoDto;
import com.wetube.wetube_service.service.VideoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/videos")
public class VideoController {

    private final VideoService videoService;
    
    @Operation(summary = "Upload a new video", description = "Creates a new video based on video file, userId, and other metadata, then returns the created video")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Video uploaded successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data")
    })

    @PostMapping(value = "/uploadFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> uploadFile(
            @RequestParam("videoFile") MultipartFile videoFile,
            @RequestParam("usersId") String usersId,
            @RequestParam(value = "thumbnailFile", required = false) MultipartFile thumbnailFile,
            @RequestParam("title") String title,
            @RequestParam(value = "videoStatus", defaultValue = "pending") String videosStatus) {
        try {
            VideoDto meta = VideoDto.builder()
                    .usersId(usersId)
                    .title(title)
                    .videosStatus(videosStatus)
                    .build();

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(videoService.createVideo(videoFile, thumbnailFile, meta));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Bad Request", "message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Server Error", "message", e.getMessage()));
        }
    }
    @Operation(summary = "Create a new tag for an uploadFileId", description = "Creates a tag based on the given entity id and returns the tag with the created data")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tag created successfully"),
        @ApiResponse(responseCode = "404", description = "uploadFileId not found for the given id")
    })
    @PostMapping(value = "/{id}/tags", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<VideoDto> addTagsToVideo(
            @PathVariable("id") UUID videoId,
            @RequestParam("hashtags") String hashtags 
    ) {
        if (hashtags == null || hashtags.isBlank()) {
            return ResponseEntity.badRequest().body(null);
        }
        return ResponseEntity.ok(videoService.addTags(videoId, hashtags));
    }

    @GetMapping
    public ResponseEntity<?> getAllVideo() {
        return ResponseEntity.ok(videoService.getAllVideo());
    }
}
