package com.wetube.wetube_service.mapper;

import com.wetube.wetube_service.dto.response.ChannelResponseDto;
import com.wetube.wetube_service.dto.response.SubscribedChannelDto;
import com.wetube.wetube_service.entity.channel.Channel;
import com.wetube.wetube_service.entity.channel.Subscription;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ChannelMapper {
    ChannelResponseDto toChannelDto(Channel channel);
    List<ChannelResponseDto> toDtoList(List<Channel> channels);

    @Mapping(target = "id", source = "id.tier.channel.id")   // channel id
    @Mapping(target = "name", source = "id.tier.channel.name")
    @Mapping(target = "avatarUrl", source = "id.tier.channel.avatarUrl")
    @Mapping(target = "subscriptionId", source = "id.tier.id") // tier.id
    SubscribedChannelDto toSubChannelDto(Subscription subscription);
}
