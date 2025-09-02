package com.wetube.wetube_service.controller;

import com.wetube.wetube_service.dto.UserDto;
import com.wetube.wetube_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser(Authentication authentication) {
        // Principal bây giờ là một đối tượng Jwt
        Jwt principal = (Jwt) authentication.getPrincipal();

        UserDto currentAppUser = userService.findOrCreateUser(principal);

        return ResponseEntity.ok(currentAppUser);
    }
}
