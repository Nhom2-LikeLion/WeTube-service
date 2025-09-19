    package com.wetube.wetube_service.entity.interaction;

    import jakarta.persistence.*;
    import lombok.*;

    import java.time.LocalDateTime;
    import java.util.UUID;

    import com.wetube.wetube_service.entity.AppUser;

    @Entity
    @Table(name = "comments")
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public class Comment {

        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        private UUID id;

        @Column(name = "target_id", nullable = false)
        private UUID targetId;

        @Column(name = "parent_comment_id")
        private UUID parentCommentId;

        @Column(columnDefinition = "TEXT")
        private String content;

        @Column(name = "like_count")
        private Integer likeCount;

        @Column(name = "created_at")
        private LocalDateTime createdAt;

        @Column(name = "updated_at")
        private LocalDateTime updatedAt;

        @Enumerated(EnumType.STRING)
        @Column(name = "target_type", nullable = false)
        private TargetType targetType;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "user_id")
        private AppUser user;

        @PrePersist
        public void prePersist() {
            if (createdAt == null)
                createdAt = LocalDateTime.now();
            updatedAt = createdAt;
            if (likeCount == null)
                likeCount = 0;
        }

        @PreUpdate
        public void preUpdate() {
            updatedAt = LocalDateTime.now();
        }

        public enum TargetType {
            POST,
            VIDEO,
            COMMENT
        }
    }
