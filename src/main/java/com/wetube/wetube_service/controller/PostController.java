package com.wetube.wetube_service.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wetube.wetube_service.dto.post.PollSummaryDto;
import com.wetube.wetube_service.dto.post.PostDto;
import com.wetube.wetube_service.dto.post.VoteRequestDto;
import com.wetube.wetube_service.service.post.PollVoteService;
import com.wetube.wetube_service.service.post.PostService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final PollVoteService pollVoteService;
    @Operation(summary = "Get posts by userId", description = "Returns the list of posts based on the given userId")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved posts for the userId"),
        @ApiResponse(responseCode = "404", description = "Server error, no specific details provided")
    })

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
    @Operation(summary = "Create a new post", description = "Creates a new post and returns the newly created post information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Post created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data")
    })

    @PostMapping
    public ResponseEntity<PostDto> createPost(@RequestBody PostDto postDto) {
        return ResponseEntity.ok(postService.createPost(postDto));
    }
    @Operation(summary = "Update post by id", description = "Updates an existing post based on the given post id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Post updated successfully"),
        @ApiResponse(responseCode = "404", description = "Post not found for update")
    })

    @PutMapping("/{id}")
    public ResponseEntity<PostDto> updatePost(@PathVariable UUID id, @RequestBody PostDto postDto) {
        return ResponseEntity.ok(postService.updatePost(id, postDto));
    }
    @Operation(summary = "Delete post by id", description = "Deletes a post based on the given post id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Post deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Post not found for deletion")
    })

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable UUID id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }
    @Operation(summary = "Create a review for a post", description = "Creates a review based on the given postId")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Review for post created successfully"),
        @ApiResponse(responseCode = "404", description = "PostId not found for review creation")
    })
    @PostMapping("/{postId}/polls/vote")
    public ResponseEntity<PollSummaryDto> vote(
            @PathVariable UUID postId,
            @RequestBody VoteRequestDto request
    ) {
        request.setPostId(postId);
        return ResponseEntity.ok(pollVoteService.vote(request));
    }
}

