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
import org.springframework.web.bind.annotation.RestController;

import com.wetube.wetube_service.dto.CommentDto.CommentRequestDto;
import com.wetube.wetube_service.dto.CommentDto.CommentResponseDto;
import com.wetube.wetube_service.entity.Comment;
import com.wetube.wetube_service.service.CommentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    @Operation(summary = "Get comments by target", description = "Retrieves a list of comments for a given target type and targetId")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Comments retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "No comments found for the given target")
    })
    @GetMapping("/{targetType}/{targetId}")
    public ResponseEntity<List<CommentResponseDto>> getCommentsByTarget(
            @PathVariable("targetId") UUID targetId,
            @PathVariable("targetType") Comment.TargetType targetType
    ) {
        return ResponseEntity.ok(commentService.getCommentsByTargetId(targetId, targetType));
    }
    @Operation(summary = "Get a comment by ID", description = "Retrieves a comment using its unique ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Comment found"),
        @ApiResponse(responseCode = "404", description = "Comment not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CommentResponseDto> getCommentById(@PathVariable UUID id) {
        return ResponseEntity.ok(commentService.getCommentById(id));
    }
    @Operation(summary = "Get replies by parentId", description = "Retrieves a list of replies for the given parent comment ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Replies retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "No replies found for the given parentId")
    })
    @GetMapping("/reply/{parentId}")
    public ResponseEntity<List<CommentResponseDto>> getReplies(@PathVariable UUID parentId) {
        return ResponseEntity.ok(commentService.getReplies(parentId));
    }
    @Operation(summary = "Create a new comment", description = "Creates a new comment and returns the created comment")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Comment created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public ResponseEntity<CommentResponseDto> createComment(@RequestBody CommentRequestDto request) {
        return ResponseEntity.ok(commentService.createComment(request));
    }
    @Operation(summary = "Update a comment", description = "Updates an existing comment by its ID and returns the updated comment")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Comment updated successfully"),
        @ApiResponse(responseCode = "404", description = "Comment not found"),
        @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CommentResponseDto> updateComment(@PathVariable UUID id, @RequestBody CommentRequestDto request) {
        return ResponseEntity.ok(commentService.updateComment(id, request));
    }
    @Operation(summary = "Delete a comment", description = "Deletes a comment by its unique ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Comment deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Comment not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable UUID id) {
        commentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }
}

