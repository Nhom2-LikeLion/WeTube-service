package com.wetube.wetube_service.entity.CompositeKey;


import com.wetube.wetube_service.entity.AppUser;
import com.wetube.wetube_service.entity.Channel.MembershipTier;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;


import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class SubscriptionId implements Serializable {
    @ManyToOne @JoinColumn(name = "user_id")
    private AppUser subscriber;

    @ManyToOne
    @JoinColumn(name = "tier_id")
    private MembershipTier tier;
}