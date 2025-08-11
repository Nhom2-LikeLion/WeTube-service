package com.wetube.wetube_service.service;

import com.wetube.wetube_service.dto.CommentDto;
import com.wetube.wetube_service.entity.Comment;

import java.util.List;
import java.util.UUID;

public interface CommentService {
    List<Comment> getCommentsByTargetId(UUID targetId);
    CommentDto getCommentById(Long id);
    CommentDto createComment(CommentDto commentDto);
    CommentDto updateComment(Long id, CommentDto commentDto);
    void deleteComment(Long id);
}


