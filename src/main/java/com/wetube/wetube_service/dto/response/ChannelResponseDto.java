package com.wetube.wetube_service.dto.response;

import com.wetube.wetube_service.enumeration.Country;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChannelResponseDto {
    private UUID id;
    private String backgroundImgUrl;
    private String name;
    private int totalSubscribers;
    private int totalVideos;
    private String description;
    private String countryCode;
    private LocalDateTime createdAt;
    private int totalViews;
    //private List<MembershipTierResponseDto> membershipTiers;
}
