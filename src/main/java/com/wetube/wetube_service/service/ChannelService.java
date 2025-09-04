package com.wetube.wetube_service.service;

import com.wetube.wetube_service.dto.UserDto;
import com.wetube.wetube_service.dto.response.ChannelResponseDto;
import com.wetube.wetube_service.dto.response.MemberTierDto;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    ChannelResponseDto getChannel(UUID channelId);
//    void updateChannel(UUID channelId, ChannelUpdateDto dto);
    List<MemberTierDto> getTiersByChannel(UUID channelId);
    void addTierToChannel(UUID channelId, MemberTierDto dto);
    List<UserDto> getSubscribers(UUID channelId);
    List<UserDto> getPremiumSubscribers(UUID channelId);
    void initiateChannel(UUID userID);
}
