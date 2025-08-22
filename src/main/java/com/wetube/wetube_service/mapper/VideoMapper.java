package com.wetube.wetube_service.mapper;

import com.wetube.wetube_service.dto.VideoDto;
import com.wetube.wetube_service.model.Tag;
import com.wetube.wetube_service.model.Video;
import com.wetube.wetube_service.model.VideoTag;
import org.mapstruct.*;
import java.util.*;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
        uses = { TagMapper.class },                 // dùng TagMapper để map Tag -> TagDto
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface VideoMapper {

    // Entity -> DTO
    // MapStruct sẽ tìm cách chuyển Set<VideoTag> -> Set<TagDto>.
    // Ta hỗ trợ bằng default method unwrap sang Set<Tag>, rồi để TagMapper lo Tag -> TagDto.
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
    @Mapping(target = "videoTags", ignore = true) // quan hệ join xử lý ở Service
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Video toEntity(VideoDto dto);

    // ---- Helpers cho MapStruct chain mapping ----

    // Chuyển Set<VideoTag> -> Set<Tag> để MapStruct gọi tiếp TagMapper(Tag -> TagDto)
    default Set<Tag> map(Set<VideoTag> videoTags) {
        if (videoTags == null || videoTags.isEmpty()) return Collections.emptySet();
        return videoTags.stream()
                .map(VideoTag::getTag)
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
