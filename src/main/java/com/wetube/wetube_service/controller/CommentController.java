package com.wetube.wetube_service.controller;

import com.wetube.wetube_service.dto.CommentDto.CommentRequestDto;
import com.wetube.wetube_service.dto.CommentDto.CommentResponseDto;
import com.wetube.wetube_service.entity.Comment;
import com.wetube.wetube_service.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/post/{targetId}")
    public ResponseEntity<List<CommentResponseDto>> getCommentsByPost(
            @PathVariable UUID targetId,
            @RequestParam(name = "type", required = false, defaultValue = "POST") Comment.TargetType type
    ) {
        return ResponseEntity.ok(commentService.getCommentsByTargetId(targetId, type));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentResponseDto> getCommentById(@PathVariable UUID id) {
        return ResponseEntity.ok(commentService.getCommentById(id));
    }

    @GetMapping("/reply/{parentId}")
    public ResponseEntity<List<CommentResponseDto>> getReplies(@PathVariable UUID parentId) {
        return ResponseEntity.ok(commentService.getReplies(parentId));
    }

    @PostMapping
    public ResponseEntity<CommentResponseDto> createComment(@RequestBody CommentRequestDto request) {
        return ResponseEntity.ok(commentService.createComment(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentResponseDto> updateComment(@PathVariable UUID id, @RequestBody CommentRequestDto request) {
        return ResponseEntity.ok(commentService.updateComment(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable UUID id) {
        commentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }
}

