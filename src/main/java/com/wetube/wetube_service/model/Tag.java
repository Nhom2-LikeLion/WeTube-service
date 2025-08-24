package com.wetube.wetube_service.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)

@Table(name = "tags", indexes = @Index(name = "uk_tag_name", columnList = "name", unique = true))
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "CHAR(16)")
    private UUID id;

    @Column(nullable = false, unique = true, length = 64)
    private String name;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @Builder.Default
    private Integer count = 0;


    @PrePersist
    void prePersist() {
        if (createdAt == null)
            createdAt = LocalDateTime.now();

        if (count == null)
            count = 0;
    }

    @Builder.Default
    @OneToMany(mappedBy = "tag", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<VideoTag> videoTags = new HashSet<>();
}
