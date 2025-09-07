package com.wetube.wetube_service.service;

//import com.wetube.wetube_service.dto.UserDto;
import com.wetube.wetube_service.dto.GoogleUser;
import com.wetube.wetube_service.dto.response.UserResponseDto;
import com.wetube.wetube_service.entity.AppUser;
//import org.springframework.security.oauth2.jwt.Jwt;

import java.util.UUID;

public interface UserService {
//    UserResponseDto findOrCreateUser(Jwt principal);
    UserResponseDto getUserById(UUID userId);
    AppUser upsertGoogleUser(GoogleUser googleUser); // find-or-create + link googleSub

    AppUser getById(UUID id);
}
