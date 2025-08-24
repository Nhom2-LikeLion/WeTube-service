package com.wetube.wetube_service.mapper;

import com.wetube.wetube_service.dto.TagDto;
import com.wetube.wetube_service.dto.VideoDto;
import com.wetube.wetube_service.model.Video;
import com.wetube.wetube_service.model.VideoTag;
import org.mapstruct.*;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface VideoMapper {

    // Entity -> DTO
    // Cho MapStruct map "videoTags" -> "tags" bằng hàm mapVideoTagSet bên dưới
    @Mapping(target = "tags", source = "videoTags")
    VideoDto toDto(Video entity);

    // List mapping
    default List<VideoDto> toDtoList(List<Video> entities) {
        if (entities == null || entities.isEmpty()) return Collections.emptyList();
        return entities.stream().map(this::toDto).toList();
    }

    // DTO -> Entity (không set quan hệ và audit ở mapper)
    @InheritInverseConfiguration(name = "toDto")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "videoTags", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Video toEntity(VideoDto dto);

    // ---------- Các mapping hỗ trợ ----------

    // Map 1 phần tử: VideoTag -> TagDto (lấy field từ vt.tag.*)
    @Named("videoTagToTagDto")
    @Mapping(target = "id",        source = "tag.id")
    @Mapping(target = "name",      source = "tag.name")
    @Mapping(target = "createdAt", source = "tag.createdAt")
    @Mapping(target = "count",     source = "tag.count")
    TagDto mapVideoTagToTagDto(VideoTag vt);

    // Map tập hợp: Set<VideoTag> -> Set<TagDto>
    @IterableMapping(qualifiedByName = "videoTagToTagDto")
    Set<TagDto> map(Set<VideoTag> videoTags);
}
