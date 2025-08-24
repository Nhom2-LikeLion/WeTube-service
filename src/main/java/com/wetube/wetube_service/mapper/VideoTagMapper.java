package com.wetube.wetube_service.mapper;

import org.mapstruct.Mapper;


import com.wetube.wetube_service.model.Tag;
import com.wetube.wetube_service.model.VideoTag;

// VideoTagMapper.java (nếu bạn đang có interface này)
@Mapper(componentModel = "spring", uses = TagMapper.class)
public interface VideoTagMapper {
    default Tag toTag(VideoTag vt) { return vt == null ? null : vt.getTag(); }
}

