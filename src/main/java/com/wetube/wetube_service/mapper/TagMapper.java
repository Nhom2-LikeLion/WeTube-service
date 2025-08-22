package com.wetube.wetube_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

import com.wetube.wetube_service.dto.TagDto;
import com.wetube.wetube_service.model.Tag;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TagMapper {

    // entity -> dto: tên trùng (id, name, createdAt, count) => MapStruct tự map, KHÔNG cần @Mapping
    TagDto toDto(Tag tag);

    // dto -> entity: bỏ qua các field do JPA/service set
    @Mappings({
        @Mapping(target = "id",        ignore = true),
        @Mapping(target = "createdAt", ignore = true),
        @Mapping(target = "count",     ignore = true)
    })
    Tag toEntity(TagDto dto);
}
