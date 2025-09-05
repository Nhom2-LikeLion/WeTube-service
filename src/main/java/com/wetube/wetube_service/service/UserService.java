package com.wetube.wetube_service.service;

//import com.wetube.wetube_service.dto.UserDto;
import com.wetube.wetube_service.dto.response.UserResponseDto;
//import org.springframework.security.oauth2.jwt.Jwt;

import java.util.UUID;

public interface UserService {
//    UserResponseDto findOrCreateUser(Jwt principal);
    UserResponseDto getUserById(UUID userId);

}
