package com.wetube.wetube_service.service.impl;

import com.wetube.wetube_service.repository.UserRepository;
import com.wetube.wetube_service.dto.response.UserResponseDto;
import com.wetube.wetube_service.entity.AppUser;
import com.wetube.wetube_service.exception.ResourceNotFoundException;
import com.wetube.wetube_service.mapper.UserMapper;
import com.wetube.wetube_service.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
//import org.springframework.security.oauth2.jwt.Jwt;

import java.util.UUID;

@AllArgsConstructor
@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto getUserById(UUID userId) {
        Optional<AppUser> userOptional = userRepository.findById(userId);

        AppUser user = userOptional.orElseThrow(() -> new ResourceNotFoundException("User","Id", userId.toString()));

        return userMapper.toDto(user);
    }

//    @Override
//    @Transactional
//    public UserResponseDto findOrCreateUser(Jwt principal) {
//        UUID userId = UUID.fromString(principal.getSubject());
//
//        AppUser userEntity = userRepository.findById(userId)
//                .orElseGet(() -> {
//                    AppUser newAppUser = new AppUser();
//                    newAppUser.setId(userId);
//                    newAppUser.setEmail(principal.getClaimAsString("email"));
//                    newAppUser.setName(principal.getClaimAsString("name"));
//                    newAppUser.setAvatarUrl(principal.getClaimAsString("picture"));
//
//                    return userRepository.save(newAppUser);
//                });
//
//        return userMapper.toDto(userEntity);
//    }
}
