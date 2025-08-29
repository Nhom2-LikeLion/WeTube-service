package com.wetube.wetube_service.mapper;

import com.wetube.wetube_service.dto.response.SubscribedChannelDto;
import com.wetube.wetube_service.entity.Channel.Subscription;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SubscriptionMapper {
    @Mapping(source = "tier.channel.id", target = "channelId")
    @Mapping(source = "tier.channel.name", target = "channelName")
    @Mapping(source = "tier.channel.avatarUrl", target = "avatarUrl")
    SubscribedChannelDto toSubscribedChannelDto(Subscription subscription);
}
