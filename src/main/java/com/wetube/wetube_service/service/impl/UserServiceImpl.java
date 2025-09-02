package com.wetube.wetube_service.service.impl;

import com.wetube.wetube_service.dto.UserDto;
import com.wetube.wetube_service.entity.AppUser;
import com.wetube.wetube_service.mapper.UserMapper;
import com.wetube.wetube_service.repository.UserRepository;
import com.wetube.wetube_service.service.UserService;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

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
    public UserDto findOrCreateUser(Jwt principal) {
        UUID userId = UUID.fromString(principal.getSubject());

        AppUser userEntity = userRepository.findById(userId)
                .orElseGet(() -> {
                    AppUser newAppUser = new AppUser();
                    newAppUser.setId(userId);
                    newAppUser.setEmail(principal.getClaimAsString("email"));
                    newAppUser.setName(principal.getClaimAsString("name"));
                    newAppUser.setAvatarUrl(principal.getClaimAsString("picture"));

                    return userRepository.save(newAppUser);
                });

        return userMapper.toDto(userEntity);
    }
}
