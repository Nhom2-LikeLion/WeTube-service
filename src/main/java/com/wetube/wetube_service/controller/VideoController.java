package com.wetube.wetube_service.controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.wetube.wetube_service.dto.response.CustomPageResponse;
import com.wetube.wetube_service.dto.video.VideoFormDetailDto;
import com.wetube.wetube_service.dto.video.VideoUpdateDto;
import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.wetube.wetube_service.dto.video.VideoDetailResponseDto;
import com.wetube.wetube_service.dto.video.VideoDto;
import com.wetube.wetube_service.search.VideoDocument;
import com.wetube.wetube_service.service.VideoSearchService;
import com.wetube.wetube_service.service.client.TranslationClient;
import com.wetube.wetube_service.service.video.VideoService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/videos")
public class VideoController {

    private final VideoService videoService;
    private final VideoSearchService searchService;
    private final TranslationClient translationClient;

    private static final PolicyFactory SANITIZER_POLICY = Sanitizers.FORMATTING.and(Sanitizers.LINKS);

    @PostMapping(value = "/uploadFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Object> uploadFile(
            @RequestParam("videoFile") MultipartFile videoFile,
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam(value = "thumbnailFile", required = false) MultipartFile thumbnailFile,
            @RequestParam("title") String title,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "duration", required = false) Float duration,
            @RequestParam(value = "tags", required = false) String tags,
            @RequestParam(value = "targetLang", required = false) String targetLang
            ) {
        try {
            UUID authenticatedUserId = UUID.fromString(jwt.getSubject());

            String sanitizedTitle = SANITIZER_POLICY.sanitize(title);
            String sanitizedDescription = (description != null) ? SANITIZER_POLICY.sanitize(description) : null;
            String sanitizedTags = (tags != null) ? SANITIZER_POLICY.sanitize(tags) : null;

            VideoDto meta = VideoDto.builder()
                    .title(sanitizedTitle)
                    .description(sanitizedDescription)
                    .duration(duration)
                    .tagsAsString(sanitizedTags)
                    .build();

            VideoDto createdVideo = videoService.createVideo(videoFile, thumbnailFile, meta, authenticatedUserId);

            String Lang = (targetLang == null || targetLang.isBlank()) ? "en" : targetLang;

             try {
                translationClient.requestTranslation(
                        createdVideo.getId(),
                        createdVideo.getVideoUrl(),
                        Lang
                );
            } catch (Exception ex) {
                // Không chặn flow chính nếu microservice lỗi
                System.err.println("⚠️ Translation service error: " + ex.getMessage());
            }
            
            return ResponseEntity.status(HttpStatus.CREATED).body(createdVideo);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Bad Request", "message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Server Error", "message", e.getMessage()));
        }
    }

    @GetMapping("/{id}/detail")
    public ResponseEntity<VideoDetailResponseDto> getDetail(@PathVariable UUID id, @AuthenticationPrincipal Jwt jwt) {
        UUID userId = (jwt != null) ? UUID.fromString(jwt.getSubject()) : null;
        return ResponseEntity.ok(videoService.getDetailWithRecommend(id, userId));
    }

    @PostMapping(value = "/{id}/tags", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<VideoDto> addTagsToVideo(
            @PathVariable("id") UUID videoId,
            @RequestParam("hashtags") String hashtags) {
        if (hashtags == null || hashtags.isBlank()) {
            return ResponseEntity.badRequest().body(null);
        }
        return ResponseEntity.ok(videoService.addTags(videoId, hashtags));
    }

    @GetMapping
    public ResponseEntity<Object> getAllVideo() {
        return ResponseEntity.ok(videoService.getAllVideo());
    }

    @GetMapping("/by-user/{userId}")
    public ResponseEntity<CustomPageResponse<VideoDto>> getVideosByUserId(
            @PathVariable UUID userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        CustomPageResponse<VideoDto> videoPage = videoService.getVideosByUserId(userId, page, size);
        return ResponseEntity.ok(videoPage);
    }

    @GetMapping("/by-tag")
    public ResponseEntity<List<VideoDto>> getVideosByTag(@RequestParam String tag) {
        return ResponseEntity.ok(videoService.getVideosByTag(tag));
    }

    // ===================== Elasticsearch Search =====================

    // Search gần đúng theo title → chỉ trả về VideoDocument
    @GetMapping("/search/videos")
    public Page<VideoDocument> searchByTitle(@RequestParam String title,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return searchService.searchByTitle(title, page, size);
    }

    // Search chính xác title
    @GetMapping("/search/videos/exact")
    public VideoDocument searchExact(@RequestParam String title) {
        return searchService.searchExactTitle(title);
    }

    // Search đầy đủ (ES + load lại từ MySQL) → trả về VideoDto đầy đủ
    @GetMapping("/search/full")
    public Page<VideoDto> searchByTitleFull(@RequestParam String title,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return searchService.searchByTitleFull(title, page, size);
    }

    // Suggest autocomplete
    @GetMapping("/search/videos/suggest")
    public List<String> suggest(@RequestParam String prefix,
            @RequestParam(defaultValue = "5") int size) {
        return searchService.suggestTitles(prefix, size);
    }

    // ===================== Detail + Update =====================

    @GetMapping("/{id}/form-details")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<VideoFormDetailDto> getVideoDetail(@PathVariable UUID id) {
        return ResponseEntity.ok(videoService.getVideoDetail(id));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<VideoDto> updateVideoDetails(
            @PathVariable UUID id,
            @RequestParam("title") String title,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam("status") String status,
            @RequestParam(value = "tags", required = false) String tags,
            @RequestParam(value = "thumbnailFile", required = false) MultipartFile thumbnailFile,
            @AuthenticationPrincipal Jwt jwt) {

        UUID authenticatedUserId = UUID.fromString(jwt.getSubject());

        VideoUpdateDto updateDto = new VideoUpdateDto();
        updateDto.setTitle(title);
        updateDto.setDescription(description);
        updateDto.setStatus(status);
        updateDto.setTags(tags);

        VideoDto updatedVideo = videoService.updateVideo(id, updateDto, thumbnailFile, authenticatedUserId);

        return ResponseEntity.ok(updatedVideo);
    }
}
