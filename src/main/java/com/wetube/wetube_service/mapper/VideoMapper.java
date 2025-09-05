package com.wetube.wetube_service.mapper;

import com.wetube.wetube_service.dto.TagDto;
import com.wetube.wetube_service.dto.VideoDto;
import com.wetube.wetube_service.entity.Video;
import com.wetube.wetube_service.entity.VideoTag;

import org.mapstruct.*;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface VideoMapper {

   
    @Mapping(target = "tags", source = "videoTags")
    VideoDto toDto(Video entity);

    default List<VideoDto> toDtoList(List<Video> entities) {
        if (entities == null || entities.isEmpty()) return Collections.emptyList();
        return entities.stream().map(this::toDto).toList();
    }

    @InheritInverseConfiguration(name = "toDto")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "videoTags", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Video toEntity(VideoDto dto);

    @Named("videoTagToTagDto")
    @Mapping(target = "id",        source = "tag.id")
    @Mapping(target = "name",      source = "tag.name")
    @Mapping(target = "createdAt", source = "tag.createdAt")
    @Mapping(target = "count",     source = "tag.count")
    TagDto mapVideoTagToTagDto(VideoTag vt);

    @IterableMapping(qualifiedByName = "videoTagToTagDto")
    Set<TagDto> map(Set<VideoTag> videoTags);
}
