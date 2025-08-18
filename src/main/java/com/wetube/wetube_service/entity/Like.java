package com.wetube.wetube_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "likes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID targetId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Like.TargetType targetType;

    private UUID userId;

    private Boolean status;

    public enum TargetType {
        POST,
        COMMENT,
        VIDEO
    }
}
