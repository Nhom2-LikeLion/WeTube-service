package com.wetube.wetube_service.service;

import com.wetube.wetube_service.dto.UserDto;
import org.springframework.security.oauth2.jwt.Jwt;
public interface UserService {
    UserDto findOrCreateUser(Jwt principal);
}
