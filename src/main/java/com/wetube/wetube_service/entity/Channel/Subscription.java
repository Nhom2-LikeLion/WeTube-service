package com.wetube.wetube_service.entity.channel;

import com.wetube.wetube_service.entity.AppUser;
import com.wetube.wetube_service.entity.compositeKey.SubscriptionId;
import com.wetube.wetube_service.enumeration.SubscriptionType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "subscriptions")
public class Subscription {

    @EmbeddedId
    private SubscriptionId id;

    @Enumerated(EnumType.STRING)
    private SubscriptionType notificationMode;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public Channel getChannel() {
        return id.getTier().getChannel();
    }
}
