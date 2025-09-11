package com.wetube.wetube_service.mapper;

import com.wetube.wetube_service.dto.response.ChannelResponseDto;
import com.wetube.wetube_service.dto.response.SubscribedChannelResponseDto;
import com.wetube.wetube_service.dto.response.UserChannelResponseDto;
import com.wetube.wetube_service.entity.channel.Channel;
import com.wetube.wetube_service.entity.channel.Subscription;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {MemberTierMapper.class})
public interface ChannelMapper {

    // User's Channel
    UserChannelResponseDto toChannelDto(Channel channel);

    // User's Subbed Channels
    @Mapping(target = "id", source = "id.tier.channel.id")   // channel id
    @Mapping(target = "name", source = "id.tier.channel.name")
    @Mapping(target = "picture", source = "id.tier.channel.picture")
    @Mapping(target = "subscriptionId", source = "id.tier.id") // tier.id
    SubscribedChannelResponseDto toSubChannelDto(Subscription subscription);

    ChannelResponseDto toChannelResponseDto(Channel channel);
}
