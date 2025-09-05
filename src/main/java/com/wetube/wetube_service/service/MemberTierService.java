package com.wetube.wetube_service.service;

import com.wetube.wetube_service.dto.response.MemberTierDto;

import java.util.UUID;

public interface MemberTierService {
    UUID addTier(UUID channelId, MemberTierDto dto);
    void updateTier(UUID tierId, MemberTierDto dto);
    void deleteTier(UUID tierId);
}
