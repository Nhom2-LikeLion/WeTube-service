package com.wetube.wetube_service.controller;

import com.nimbusds.jwt.JWTClaimsSet;
import com.wetube.wetube_service.dto.UserDto;
import com.wetube.wetube_service.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api")
public class ClerkController {
    private final UserService userService;

    public ClerkController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser(Authentication authentication) {// Lấy lại đối tượng claims mà chúng ta đã đặt trong filter
        JWTClaimsSet claims = (JWTClaimsSet) authentication.getPrincipal();

        // Gọi service như cũ
        UserDto currentAppUser = userService.findOrCreateUser(claims);

        return ResponseEntity.ok(currentAppUser);
    }
}
