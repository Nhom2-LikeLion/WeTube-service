package com.wetube.wetube_service.entity.Channel;

import com.wetube.wetube_service.entity.AppUser;
import com.wetube.wetube_service.entity.CompositeKey.SubscriptionId;
import com.wetube.wetube_service.enumeration.SubscriptionType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "subscriptions")
public class Subscription {

    @EmbeddedId
    private SubscriptionId id;

//    @ManyToOne
//    @MapsId("subscriberId")
//    @JoinColumn(name = "user_id")
//    private AppUser subscriber;
//
//    @ManyToOne
//    @MapsId("tierId")
//    @JoinColumn(name = "tier_id")
//    private MembershipTier tier; // Free or Membership

    @Enumerated(EnumType.STRING)
    private SubscriptionType notificationMode;

    @CreationTimestamp
    private LocalDateTime createdAt;

}
