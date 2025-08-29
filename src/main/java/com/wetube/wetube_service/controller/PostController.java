package com.wetube.wetube_service.controller;

import com.wetube.wetube_service.dto.post.PollSummaryDto;
import com.wetube.wetube_service.dto.post.PostDto;
import com.wetube.wetube_service.dto.post.VoteRequestDto;
import com.wetube.wetube_service.service.post.PollVoteService;
import com.wetube.wetube_service.service.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final PollVoteService pollVoteService;

    @GetMapping("/{userId}/posts")
    public ResponseEntity<List<PostDto>> getPostsByUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(postService.getPostsByUser(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPostById(
            @PathVariable UUID id,
            @RequestParam(required = false) UUID viewerId
    ) {
        return ResponseEntity.ok(postService.getPostById(id));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PostDto> createPost(
            @RequestPart("postDto") PostDto postDto,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        try {
            if (image != null && !image.isEmpty()) {
                String filename = UUID.randomUUID() + "_" + image.getOriginalFilename();
                Path filePath = Paths.get("uploads").resolve(filename);

                Files.createDirectories(filePath.getParent());

                image.transferTo(filePath.toFile());

                postDto.setImageUrl("/uploads/" + filename);
            }
        } catch (Exception e) {
            throw new RuntimeException("Upload file thất bại", e);
        }

        return ResponseEntity.ok(postService.createPost(postDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostDto> updatePost(@PathVariable UUID id, @RequestBody PostDto postDto) {
        return ResponseEntity.ok(postService.updatePost(id, postDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable UUID id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{postId}/polls/vote")
    public ResponseEntity<PollSummaryDto> vote(
            @PathVariable UUID postId,
            @RequestBody VoteRequestDto request
    ) {
        request.setPostId(postId);
        return ResponseEntity.ok(pollVoteService.vote(request));
    }
}

