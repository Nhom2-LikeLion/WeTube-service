package com.wetube.wetube_service.service.impl;

import org.springframework.stereotype.Service;

import com.wetube.wetube_service.dto.TagDto;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import com.wetube.wetube_service.service.TagService;

@Service
@Transactional
@AllArgsConstructor
public class TagServiceXmpl implements TagService {

    @Override
    public TagDto createTag(TagDto tagDto) throws Exception {
        return tagDto;
        
    }
    
}
