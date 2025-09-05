package com.wetube.wetube_service.mapper;

import org.mapstruct.Mapper;

import com.wetube.wetube_service.entity.VideoTag;
import com.wetube.wetube_service.entity.Tag;

@Mapper(componentModel = "spring", uses = TagMapper.class)
public interface VideoTagMapper {
    default Tag toTag(VideoTag vt) { return vt == null ? null : vt.getTag(); }
}

