package com.wetube.wetube_service.service.impl;

import com.wetube.wetube_service.Repository.UserRepository;
import com.wetube.wetube_service.dto.UserDto;
import com.wetube.wetube_service.dto.response.UserResponseDto;
import com.wetube.wetube_service.entity.AppUser;
import com.wetube.wetube_service.exception.ResourceNotFoundException;
import com.wetube.wetube_service.mapper.UserMapper;
import com.wetube.wetube_service.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
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

        AppUser user = userOptional.orElseThrow(() -> new ResourceNotFoundException("Can't find user with id: " + userId));

        return userMapper.toDto(user);
    }
}
