package com.wetube.wetube_service.service.impl;

import com.nimbusds.jwt.JWTClaimsSet;
import com.wetube.wetube_service.dto.UserDto;
import com.wetube.wetube_service.entity.AppUser;
import com.wetube.wetube_service.mapper.UserMapper;
import com.wetube.wetube_service.Repository.UserRepository;
import com.wetube.wetube_service.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    @Transactional
    public UserDto findOrCreateUser(JWTClaimsSet claims) {
        String clerkId = claims.getSubject();

        AppUser userEntity = userRepository.findByClerkId(clerkId)
                .orElseGet(() -> {
                    AppUser newAppUser = new AppUser();
                    newAppUser.setClerkId(clerkId);
                    newAppUser.setEmail((String) claims.getClaim("email"));
                    newAppUser.setName((String) claims.getClaim("name"));
                    newAppUser.setAvatarUrl((String) claims.getClaim("picture"));

                    return userRepository.save(newAppUser);
                });

        return userMapper.toDto(userEntity);
    }
}