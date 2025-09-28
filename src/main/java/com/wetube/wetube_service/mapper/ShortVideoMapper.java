package com.wetube.wetube_service.mapper;

import com.wetube.wetube_service.dto.ShortVideoDto;
import com.wetube.wetube_service.entity.shorts.ShortsVideo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ShortVideoMapper {

    @Mapping(source = "channel.id", target = "channelId")
    @Mapping(source = "channel.name", target = "channelName")
    @Mapping(source = "channel.picture", target = "channelPicture")
    ShortVideoDto toShortDto(ShortsVideo video);

    @Mapping(target = "channel", ignore = true)
    ShortsVideo toEntity(ShortVideoDto dto);
}

