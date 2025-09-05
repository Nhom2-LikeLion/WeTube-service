package com.wetube.wetube_service.mapper;

import com.wetube.wetube_service.dto.response.ChannelResponseDto;
import com.wetube.wetube_service.dto.response.UserChannelResponseDto;
import com.wetube.wetube_service.dto.response.SubscribedChannelDto;
import com.wetube.wetube_service.entity.channel.Channel;
import com.wetube.wetube_service.entity.channel.Subscription;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ChannelMapper {

    // User's Channel
    UserChannelResponseDto toChannelDto(Channel channel);

    // User's Subbed Channels
    @Mapping(target = "id", source = "id.tier.channel.id")   // channel id
    @Mapping(target = "name", source = "id.tier.channel.name")
    @Mapping(target = "avatarUrl", source = "id.tier.channel.avatarUrl")
    @Mapping(target = "subscriptionId", source = "id.tier.id") // tier.id
    SubscribedChannelDto toSubChannelDto(Subscription subscription);

    ChannelResponseDto toChannelResDto(Channel channel);
}
