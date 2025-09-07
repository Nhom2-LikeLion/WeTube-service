package com.wetube.wetube_service.service.impl;

import com.wetube.wetube_service.dto.GoogleUser;
import com.wetube.wetube_service.entity.Role;
import com.wetube.wetube_service.repository.RoleRepository;
import com.wetube.wetube_service.repository.UserRepository;
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
//import org.springframework.security.oauth2.jwt.Jwt;

import java.util.UUID;

@AllArgsConstructor
@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;

    private static final String DEFAULT_ROLE = "ROLE_USER";

    @Override
    public UserResponseDto getUserById(UUID userId) {
        Optional<AppUser> userOptional = userRepository.findById(userId);

        AppUser user = userOptional.orElseThrow(() -> new ResourceNotFoundException("User","Id", userId.toString()));

        return userMapper.toDto(user);
    }

    @Transactional
    @Override
    public AppUser upsertGoogleUser(GoogleUser googleUser) {
        // 1) Tồn tại theo googleSub → update nhẹ thông tin
        AppUser user = userRepository.findByGoogleSub(googleUser.sub()).orElse(null);
        if (user != null) {
            boolean dirty = false;
            if (googleUser.email() != null && !googleUser.email().equals(user.getEmail())) {
                user.setEmail(googleUser.email());
                dirty = true;
            }
            if (googleUser.name() != null && !googleUser.name().equals(user.getName())) {
                user.setName(googleUser.name());
                dirty = true;
            }
            if (googleUser.picture() != null && !googleUser.picture().equals(user.getPicture())) {
                user.setPicture(googleUser.picture());
                dirty = true;
            }

            // đảm bảo user có role mặc định
            ensureHasRole(user, DEFAULT_ROLE);

            // load đầy đủ roles trước khi return để tránh LazyInitialization ở controller
            return userRepository.findByIdWithRoles(user.getId()).orElse(user);
        }

        // 2) Chưa link sub nhưng đã có user theo email → link googleSub & bổ sung thông
        // tin
        user = userRepository.findByEmail(googleUser.email()).orElse(null);
        if (user != null) {
            user.setGoogleSub(googleUser.sub());
            if (user.getName() == null)
                user.setName(googleUser.name());
            if (user.getPicture() == null)
                user.setPicture(googleUser.picture());

            ensureHasRole(user, DEFAULT_ROLE);
            userRepository.save(user);
            return userRepository.findByIdWithRoles(user.getId()).orElse(user);
        }

        // 3) Tạo mới hoàn toàn
        AppUser created = new AppUser();
        created.setEmail(googleUser.email());
        created.setName(googleUser.name());
        created.setPicture(googleUser.picture());
        created.setGoogleSub(googleUser.sub());

        userRepository.save(created); // cần có id trước khi tạo UserRole
        ensureHasRole(created, DEFAULT_ROLE);

        // trả về kèm roles đã fetch
        return userRepository.findByIdWithRoles(created.getId()).orElse(created);
    }

    /** Gán role nếu user chưa có role code đó */
    private void ensureHasRole(AppUser user, String roleCode) {
        // nếu đã có rồi thì thôi
        boolean hasIt = user.getUserRoles().stream()
                .anyMatch(ur -> roleCode.equals(ur.getRole().getCode()));
        if (hasIt)
            return;

        Role role = roleRepository.findByCode(roleCode)
                .orElseThrow(() -> new IllegalStateException("Missing role code: " + roleCode));
        user.addRole(role); // helper trong entity User tạo UserRole(user, role)
        // không cần repo.save ở đây nếu đang trong @Transactional và user là managed
    }

    @Override
    @Transactional(readOnly = true)
    public AppUser getById(UUID id) {
        if (id == null)
            throw new IllegalArgumentException("User ID cannot be null");
        return userRepository.findByIdWithRoles(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));
    }
}
