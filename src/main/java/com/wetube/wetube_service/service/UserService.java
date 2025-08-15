package com.wetube.wetube_service.service;

import com.wetube.wetube_service.dto.response.UserResponseDto;

import java.util.UUID;

public interface UserService {
    UserResponseDto getUserById(UUID userId);
}
