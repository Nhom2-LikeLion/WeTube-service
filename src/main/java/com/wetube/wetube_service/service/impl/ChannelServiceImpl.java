package com.wetube.wetube_service.service.impl;

import com.wetube.wetube_service.dto.response.ChannelResponseDto;
import com.wetube.wetube_service.entity.AppUser;
import com.wetube.wetube_service.entity.channel.Channel;
import com.wetube.wetube_service.entity.channel.MembershipTier;
import com.wetube.wetube_service.enumeration.ActiveStatus;
import com.wetube.wetube_service.exception.ChannelAlreadyExistsException;
import com.wetube.wetube_service.exception.ResourceNotFoundException;
import com.wetube.wetube_service.mapper.ChannelMapper;
import com.wetube.wetube_service.repository.ChannelRepository;
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
    private final ChannelRepository channelRepository;
    private final ChannelMapper channelMapper;

    private final GeoIPService geoIPService;

    @Override
    public ChannelResponseDto getChannelById(UUID channelId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new ResourceNotFoundException("Channel", "id", channelId.toString()));

        return channelMapper.toChannelResponseDto(channel);
    }

    @Override
    public void initiateChannel(UUID userID, String ip) {
        AppUser user = userRepository.findById(userID)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userID.toString()));

        if (user.getChannel() != null) {
            throw new ChannelAlreadyExistsException("User with id " + userID + " already has a channel.");
        }

        String countryCode = geoIPService.getCountryCode(ip).orElse("UN"); // UN = Unknown
        Channel channel = Channel.builder()
                .name(user.getName() != null ? user.getName() : "New Channel")
                .picture(user.getPicture())
                .status(ActiveStatus.ACTIVE)
                .countryCode(countryCode)
                .totalSubscribers(0)
                .totalVideos(0)
                .totalViews(0)
                .revenue(0f)
                .build();

        MembershipTier defaultTier = MembershipTier.builder()
                .title("Default Tier")
                .description("Default membership tier")
                .price(0f)
                .defaultTier(true)
                .channel(channel)
                .build();

        channel.setMembershipTiers(List.of(defaultTier));
        user.setChannel(channel);
        userRepository.save(user);
    }
}
