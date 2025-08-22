package com.wetube.wetube_service.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

import com.wetube.wetube_service.dto.VideoTagDto;
import com.wetube.wetube_service.model.VideoTag;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface VideoTagMapper {

    @Mappings({
        @Mapping(target = "videoId",    source = "video.id"),
        @Mapping(target = "tagId",      source = "tag.id"),
        @Mapping(target = "tagName",    source = "tag.name"),
        @Mapping(target = "videoTitle", source = "video.title")
    })
    VideoTagDto toDto(VideoTag entity);

    List<VideoTagDto> toDtoList(List<VideoTag> entities);
}
