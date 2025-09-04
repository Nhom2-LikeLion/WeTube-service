package com.wetube.wetube_service.service.impl;

import com.wetube.wetube_service.dto.UserDto;
import com.wetube.wetube_service.dto.response.ChannelResponseDto;
import com.wetube.wetube_service.dto.response.MemberTierDto;
import com.wetube.wetube_service.entity.AppUser;
import com.wetube.wetube_service.entity.channel.Channel;
import com.wetube.wetube_service.entity.channel.MembershipTier;
import com.wetube.wetube_service.enumeration.ActiveStatus;
import com.wetube.wetube_service.enumeration.Country;
import com.wetube.wetube_service.exception.ChannelAlreadyExistsException;
import com.wetube.wetube_service.exception.ResourceNotFoundException;
import com.wetube.wetube_service.repository.UserRepository;
import com.wetube.wetube_service.service.ChannelService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Service
@Transactional
public class ChannelServiceImpl implements ChannelService {
    private final UserRepository userRepository;

    @Override
    public ChannelResponseDto getChannel(UUID channelId) {
        return null;
    }

    @Override
    public List<MemberTierDto> getTiersByChannel(UUID channelId) {
        return List.of();
    }

    @Override
    public void addTierToChannel(UUID channelId, MemberTierDto dto) {

    }

    @Override
    public List<UserDto> getSubscribers(UUID channelId) {
        return List.of();
    }

    @Override
    public List<UserDto> getPremiumSubscribers(UUID channelId) {
        return List.of();
    }

    @Override
    public void initiateChannel(UUID userID) {
        AppUser user = userRepository.findById(userID)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userID.toString()));

        if (user.getChannel() != null) {
            throw new ChannelAlreadyExistsException("User with id " + userID + " already has a channel.");
        }

        Channel channel = Channel.builder()
                .name(user.getName() != null ? user.getName() : "New Channel")
                .avatarUrl(user.getAvatarUrl())
                .status(ActiveStatus.ACTIVE) // giả định trạng thái mặc định là ACTIVE
                .country(Country.NONE)   // hoặc null/default tuỳ yêu cầu
                .totalSubscribers(0)
                .totalVideos(0)
                .totalViews(0)
                .revenue(0f)
                .build();

        MembershipTier defaultTier = MembershipTier.builder()
                .title("Default Tier")
                .description("Default membership tier")
                .price(0f)
                .isDefault(true)
                .channel(channel)
                .build();

        channel.setMembershipTiers(List.of(defaultTier));
        user.setChannel(channel);
        userRepository.save(user);
    }
}
