package com.wetube.wetube_service.entity;

import com.wetube.wetube_service.entity.Channel.Channel;
import com.wetube.wetube_service.entity.Channel.Subscription;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "users")
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "VARCHAR(36)")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String clerkId;

    @Column(unique = true, nullable = false)
    private String email;

    private String name;
    private String avatarUrl;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "channel_id",unique = true)
    private Channel channel;
}
