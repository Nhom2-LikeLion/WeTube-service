package com.wetube.wetube_service.controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.wetube.wetube_service.dto.video.VideoDto;
import com.wetube.wetube_service.search.VideoDocument;
import com.wetube.wetube_service.service.VideoSearchService;
import com.wetube.wetube_service.service.video.VideoService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/videos")
public class VideoController {

    private final VideoService videoService;
    private final VideoSearchService searchService;

    @PostMapping(value = "/uploadFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Object> uploadFile(
            @RequestParam("videoFile") MultipartFile videoFile,
//            @RequestParam("usersId") String usersId,
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam(value = "thumbnailFile", required = false) MultipartFile thumbnailFile,
            @RequestParam("title") String title,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "duration", required = false) Float duration,
            @RequestParam(value = "tags", required = false) String tags){
        try {
            String usersId = jwt.getSubject();

            VideoDto meta = VideoDto.builder()
                    .usersId(usersId)
                    .title(title)
                    .description(description)
                    .duration(duration)
                    .tagsAsString(tags)
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
    public ResponseEntity<Object> getAllVideo() {
        return ResponseEntity.ok(videoService.getAllVideo());
    }


    @GetMapping("/search/fulltext")
	public Page<VideoDocument> fullText(@RequestParam String q, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
		return searchService.fullText(q, page, size);
	}

	@GetMapping("/search/fuzzy")
	public Page<VideoDocument> fuzzy(@RequestParam String q, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
		return searchService.fuzzy(q, page, size);
	}

	@GetMapping("/search/suggest")
	public List<String> suggest(@RequestParam String prefix, @RequestParam(defaultValue = "10") int size) {
		return searchService.suggestNames(prefix, size);
	}

	@GetMapping("/search/sort")
	public Page<VideoDocument> sort(@RequestParam String q, @RequestParam String sortField, @RequestParam(defaultValue = "true") boolean asc, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
		return searchService.sortAndPaginate(q, sortField, asc, page, size);
	}

	@GetMapping("/search/aggregate")
	public Object aggregate(@RequestParam String q) {
		return searchService.aggregateByCategory(q);
	}

	@GetMapping("/search/multi")
	public Page<VideoDocument> multi(@RequestParam(required = false) String tag,@RequestParam(required = false) String title, @RequestParam(required = false) String description, @RequestParam(required = false) String category, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
		return searchService.multiFieldSearch( tag,  title,  description,  category, page,size);
	}

    // Tag/category specific search endpoints
    @GetMapping("/search/by-tag")
    public Page<VideoDocument> byTag(@RequestParam String tag,
                                     @RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "10") int size) {
        return searchService.byTag(tag, page, size);
    }

    @GetMapping("/search/by-category")
    public Page<VideoDocument> byCategory(@RequestParam String category,
                                          @RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "10") int size) {
        return searchService.byCategory(category, page, size);
    }

    @GetMapping("/search/by-tag-category")
    public Page<VideoDocument> byTagAndCategory(@RequestParam String tag,
                                                @RequestParam String category,
                                                @RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "10") int size) {
        return searchService.byTagAndCategory(tag, category, page, size);
    }

	// Database search endpoints
	@GetMapping("/db/search/name")
	public List<VideoDto> searchByTitle(@RequestParam String title) {
		return videoService.searchByTitle(title);
	}

	@GetMapping("/db/search/name-paged")
	public Page<VideoDto> searchByNamePaged(@RequestParam String title, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
		return videoService.searchByTitlePaging(title, page, size);
	}
}

