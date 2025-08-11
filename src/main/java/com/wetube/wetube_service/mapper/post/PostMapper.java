package com.wetube.wetube_service.mapper.post;

import com.wetube.wetube_service.dto.post.PostDto;
import com.wetube.wetube_service.entity.post.Post;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PostMapper {
    PostDto toDto(Post post);
    Post toEntity(PostDto postDto);
}

