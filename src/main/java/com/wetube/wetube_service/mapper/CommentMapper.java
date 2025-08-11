package com.wetube.wetube_service.mapper;

import com.wetube.wetube_service.dto.CommentDto;
import com.wetube.wetube_service.entity.Comment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    CommentDto toDto(Comment comment);
    Comment toEntity(CommentDto commentDto);
}

