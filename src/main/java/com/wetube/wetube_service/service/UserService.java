package com.wetube.wetube_service.service;

import com.nimbusds.jwt.JWTClaimsSet;
import com.wetube.wetube_service.entity.AppUser;

public interface UserService {
    AppUser findOrCreateUser(JWTClaimsSet claims);
}
