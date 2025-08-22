package com.wetube.wetube_service.service;

import com.wetube.wetube_service.dto.TagDto;

public interface TagService {
    TagDto createTag(TagDto tagDto) throws Exception;
}
