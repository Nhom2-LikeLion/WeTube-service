package com.wetube.wetube_service.service;

import com.nimbusds.jwt.JWTClaimsSet;
import com.wetube.wetube_service.dto.UserDto;

public interface UserService {
    UserDto findOrCreateUser(JWTClaimsSet claims);
}
