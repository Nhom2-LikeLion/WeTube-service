package com.wetube.wetube_service.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wetube.wetube_service.dto.GoogleUser;
import com.wetube.wetube_service.entity.auth.OAuthAccount;
import com.wetube.wetube_service.entity.auth.Role;
import com.wetube.wetube_service.repository.auth.OAuthAccountRepository;
import com.wetube.wetube_service.repository.auth.RoleRepository;
import com.wetube.wetube_service.repository.UserRepository;
import com.wetube.wetube_service.dto.response.UserResponseDto;
import com.wetube.wetube_service.entity.AppUser;
import com.wetube.wetube_service.exception.ResourceNotFoundException;
import com.wetube.wetube_service.mapper.UserMapper;
import com.wetube.wetube_service.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
@Service
@Transactional
public class UserServiceImpl implements UserService {
    private static final String DEFAULT_ROLE = "ROLE_USER";

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private final OAuthAccountRepository oauthAccountRepository;
    private final ObjectMapper objectMapper;

    @Override
    public UserResponseDto getUserById(UUID userId) {
        Optional<AppUser> userOptional = userRepository.findById(userId);

        AppUser user = userOptional.orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId.toString()));

        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    public AppUser upsertGoogleUser(GoogleUser googleUser, String scopes) {
        Optional<OAuthAccount> oauthAccountOpt = oauthAccountRepository.findByProviderAndProviderUserId("google", googleUser.sub());

        if (oauthAccountOpt.isPresent()) {
            AppUser existingUser = oauthAccountOpt.get().getUser();
            ensureHasRole(existingUser);
            return existingUser;
        } else {
            AppUser user = userRepository.findByEmail(googleUser.email())
                    .orElseGet(() -> createNewGoogleUser(googleUser));
            ensureHasRole(user);

            OAuthAccount newAuthAccount = createOAuthAccount(googleUser, scopes, user);
            oauthAccountRepository.save(newAuthAccount);

            return user;
        }
    }

    private AppUser createNewGoogleUser(GoogleUser googleUser) {
        Role userRole = roleRepository.findByCode(DEFAULT_ROLE)
                .orElseThrow(() -> new IllegalStateException("ROLE_USER not found. Please seed the database."));

        AppUser newUser = AppUser.builder()
                .email(googleUser.email())
                .name(googleUser.name())
                .picture(googleUser.picture())
                .build();

        newUser.addRole(userRole);
        return userRepository.save(newUser);
    }

    private OAuthAccount createOAuthAccount(GoogleUser googleUser, String scopes, AppUser user) {
        OAuthAccount newAuthAccount = new OAuthAccount();
        newAuthAccount.setProvider("google");
        newAuthAccount.setProviderUserId(googleUser.sub());
        newAuthAccount.setUser(user);
        newAuthAccount.setScopes(scopes);

        try {
            String rawInfoJson = objectMapper.writeValueAsString(googleUser);
            newAuthAccount.setRawInfo(rawInfoJson);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize GoogleUser to JSON for user: {}", googleUser.email(), e);
            newAuthAccount.setRawInfo("{\"error\":\"Serialization failed\"}");
        }
        return newAuthAccount;
    }


    private void ensureHasRole(AppUser user) {
        boolean hasRole = user.getUserRoles().stream()
                .anyMatch(ur -> UserServiceImpl.DEFAULT_ROLE.equals(ur.getRole().getCode()));
        if (hasRole)
            return;

        Role role = roleRepository.findByCode(UserServiceImpl.DEFAULT_ROLE)
                .orElseThrow(() -> new IllegalStateException("Missing role code: " + UserServiceImpl.DEFAULT_ROLE));
        user.addRole(role);
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
