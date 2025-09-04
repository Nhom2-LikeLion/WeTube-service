package com.wetube.wetube_service.entity.channel;

import com.wetube.wetube_service.enumeration.ActiveStatus;
import com.wetube.wetube_service.enumeration.Country;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "channels")
public class Channel {
    @Id
    @GeneratedValue(strategy =  GenerationType.UUID)
    @Column(columnDefinition = "VARCHAR(36)")
    private UUID id;

    private String backgroundImgUrl;
    private String description;

    @Enumerated(EnumType.STRING)
    private Country country;
    @Enumerated(EnumType.STRING)
    private ActiveStatus status;

    private float revenue;
    private int totalSubscribers;
    private int totalVideos;
    private int totalViews;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private String name;
}