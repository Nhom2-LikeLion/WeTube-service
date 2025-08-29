package com.wetube.wetube_service.mapper;

import com.wetube.wetube_service.dto.response.ChannelResponseDto;
import com.wetube.wetube_service.dto.response.SubscribedChannelDto;
import com.wetube.wetube_service.entity.Channel.Channel;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ChannelMapper {
    ChannelResponseDto toChannelDto(Channel channel);
    SubscribedChannelDto toSubChannelDto(Channel channel);
    List<ChannelResponseDto> toDtoList(List<Channel> channels);
}
