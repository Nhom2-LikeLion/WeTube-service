package com.wetube.wetube_service.repository;

import com.wetube.wetube_service.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByTargetTypeAndTargetIdOrderByCreatedAtDesc(Comment.TargetType targetType, UUID targetId);
}

