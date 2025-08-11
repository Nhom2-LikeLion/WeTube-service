package com.wetube.wetube_service.service;

import com.wetube.wetube_service.dto.post.PostDto;
import java.util.List;

public interface PostService {
    List<PostDto> getPostsByUser(Long userId);
    PostDto getPostById(Long id);
    PostDto createPost(PostDto postDto);
    PostDto updatePost(Long id, PostDto postDto);
    void deletePost(Long id);
}

