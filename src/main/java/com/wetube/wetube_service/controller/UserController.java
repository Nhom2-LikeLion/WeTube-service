package com.wetube.wetube_service.controller;

import com.wetube.wetube_service.dto.UserDto;
import com.wetube.wetube_service.dto.response.UserResponseDto;
import com.wetube.wetube_service.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/{id}")
    public UserResponseDto getUserById(@PathVariable UUID id) {
        return userService.getUserById(id);
    }

//    @GetMapping("/me")
//    public ResponseEntity<UserResponseDto> getCurrentUser(Authentication authentication) {
//        // Principal bây giờ là một đối tượng Jwt
//        Jwt principal = (Jwt) authentication.getPrincipal();
//
//        UserResponseDto currentAppUser = userService.findOrCreateUser(principal);
//
//        return ResponseEntity.ok(currentAppUser);
//    }
}
