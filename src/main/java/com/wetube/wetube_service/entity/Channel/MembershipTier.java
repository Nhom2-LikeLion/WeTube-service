package com.wetube.wetube_service.entity.Channel;

import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
public class MembershipTier {
    @Id
    @GeneratedValue(strategy =   GenerationType.UUID)
    private UUID id;

    private String title;
    private float price;
    private String description;

//    @ManyToOne
//    @JoinColumn(name = "channel_id")
//    private Channel channel;

//    @OneToMany(mappedBy = "tier")
//    private List<Subscription> subscriptions;
}
