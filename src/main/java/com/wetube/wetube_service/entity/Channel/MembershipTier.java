package com.wetube.wetube_service.entity.Channel;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tiers")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MembershipTier {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "VARCHAR(36)")
    private UUID id;

    private String title;
    private float price;
    private String description;

    @Column(name = "is_default", nullable = false)
    private boolean isDefault = false;

    @ManyToOne
    @JoinColumn(name = "channel_id")
    private Channel channel;

    @OneToMany(mappedBy = "tier")
    private List<Subscription> subscriptions;
}
