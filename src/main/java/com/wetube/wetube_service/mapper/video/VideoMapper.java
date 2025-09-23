package com.wetube.wetube_service.mapper.video;

import com.wetube.wetube_service.dto.video.TagDto;
import com.wetube.wetube_service.dto.video.VideoDto;
import com.wetube.wetube_service.dto.video.VideoFormDetailDto;
import com.wetube.wetube_service.entity.video.Video;
import com.wetube.wetube_service.entity.video.VideoTag;
import com.wetube.wetube_service.search.VideoDocument;
import com.wetube.wetube_service.dto.video.RecommendVideoDto;


import org.mapstruct.*;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface VideoMapper {

   
    @Mapping(target = "tags", source = "videoTags")
    VideoDto toDto(Video entity);

    default List<VideoDto> toDtoList(List<Video> entities) {
        if (entities == null || entities.isEmpty()) return Collections.emptyList();
        return entities.stream().map(this::toDto).toList();
    }

    @Mapping(target = "id",          source = "id")
    @Mapping(target = "title",       source = "title")
    @Mapping(target = "thumbnailUrl",source = "thumbnailUrl")
    @Mapping(target = "totalView",   source = "totalView")
    @Mapping(target = "createAt",    source = "createdAt")
    @Mapping(target = "name",        source = "user.name")     
    @Mapping(target = "picture",     source = "user.picture")  
    @Mapping(target = "duration",    source = "duration")
    RecommendVideoDto toRecommendDto(Video entity);

    default List<RecommendVideoDto> toRecommendDtoList(List<Video> entities) {
        if (entities == null || entities.isEmpty()) return Collections.emptyList();
        return entities.stream().map(this::toRecommendDto).toList();
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

    @Mapping(target = "id", expression = "java(uuidToString(entity.getId()))")
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "tags", expression = "java(mapTagNames(entity.getVideoTags()))")
    VideoDocument mapEntityToVideoDocument(Video entity);

    @Named("uuidToString")
    default String uuidToString(UUID id) {
        return id != null ? id.toString() : null;
    }

    VideoFormDetailDto toFormDetailDto(Video video);

    // Helper to map Set<VideoTag> -> List<String> tag names (lowercase)
    default java.util.List<String> mapTagNames(Set<VideoTag> videoTags) {
        if (videoTags == null || videoTags.isEmpty()) return java.util.Collections.emptyList();
        return videoTags.stream()
                .filter(vt -> vt.getTag() != null && vt.getTag().getName() != null)
                .map(vt -> vt.getTag().getName().trim().toLowerCase())
                .distinct()
                .toList();
    }
}
