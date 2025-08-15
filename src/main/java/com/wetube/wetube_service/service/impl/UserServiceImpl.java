package com.wetube.wetube_service.service.impl;

import com.nimbusds.jwt.JWTClaimsSet;
import com.wetube.wetube_service.entity.AppUser;
import com.wetube.wetube_service.repository.UserRepository;
import com.wetube.wetube_service.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override // ✅ Thêm annotation @Override
    @Transactional
    public AppUser findOrCreateUser(JWTClaimsSet claims) {
        String clerkId = claims.getSubject();

        return userRepository.findByClerkId(clerkId)
                .orElseGet(() -> {
                    AppUser newAppUser = new AppUser();
                    newAppUser.setClerkId(clerkId);
                    newAppUser.setEmail((String) claims.getClaim("email"));
                    newAppUser.setName((String) claims.getClaim("name"));
                    newAppUser.setAvatarUrl((String) claims.getClaim("picture"));

                    return userRepository.save(newAppUser);
                });
    }
}
