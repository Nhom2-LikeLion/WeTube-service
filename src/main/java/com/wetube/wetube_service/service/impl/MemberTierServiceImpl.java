package com.wetube.wetube_service.service.impl;

import com.wetube.wetube_service.dto.response.MemberTierResponseDto;
import com.wetube.wetube_service.entity.channel.Channel;
import com.wetube.wetube_service.entity.channel.MembershipTier;
import com.wetube.wetube_service.exception.ResourceNotFoundException;
import com.wetube.wetube_service.mapper.MemberTierMapper;
import com.wetube.wetube_service.repository.ChannelRepository;
import com.wetube.wetube_service.repository.MemberTierRepository;
import com.wetube.wetube_service.service.MemberTierService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
public class MemberTierServiceImpl implements MemberTierService {
    private final ChannelRepository channelRepository;
    private final MemberTierRepository tierRepository;
    private final MemberTierMapper mapper;

    @Override
    public UUID addTier(UUID channelId, MemberTierResponseDto dto) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new ResourceNotFoundException("Channel", "id", channelId.toString()));

        MembershipTier tier = mapper.toEntity(dto);
        tier.setChannel(channel);
        tier.setDefaultTier(false);

        MembershipTier saved = tierRepository.save(tier);
        return saved.getId();
    }

    @Override
    public void updateTier(UUID tierId, MemberTierResponseDto dto) {
        MembershipTier tier = tierRepository.findById(tierId)
                .orElseThrow(() -> new ResourceNotFoundException("MembershipTier", "id", tierId.toString()));

        tier.setTitle(dto.getTitle());
        tier.setDescription(dto.getDescription());
        tier.setPrice(dto.getPrice());
        tier.setDefaultTier(false);

        tierRepository.save(tier);
    }

    @Override
    public void deleteTier(UUID tierId) {
        if (!tierRepository.existsById(tierId)) {
            throw new ResourceNotFoundException("MembershipTier", "id", tierId.toString());
        }
        tierRepository.deleteById(tierId);
    }
}
