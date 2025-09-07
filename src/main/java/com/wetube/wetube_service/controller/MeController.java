package com.wetube.wetube_service.controller;

import com.wetube.wetube_service.service.token.RefreshTokenService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/me")
@AllArgsConstructor
public class MeController {
    //    @GetMapping
//    public Map<String, Object> me(@AuthenticationPrincipal Jwt jwt) {
//        if (jwt == null) {
//            return Map.of(
//                    "error_code", "unauthenticated"
//            );
//        }
//        return Map.of(
//                "userId", jwt.getSubject(),
//                "email", jwt.getClaim("email"),
//                "roles", jwt.getClaim("roles"));
//    }
    private final RefreshTokenService refreshTokenService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> me(
            @AuthenticationPrincipal Jwt jwt,
            @CookieValue(name = "SID", required = false) String sid) {
        if (jwt == null) {
            if (sid != null && !sid.isBlank()) {
                try {
                    var session = refreshTokenService.validateBySession(sid);
                    if (session.isRevoked()) {
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                                "error", "Đã đăng xuất",
                                "error_code", "session_revoked",
                                "message", "Session has been revoked"
                        ));
                    }
                    // Nếu session hợp lệ, gợi ý làm mới token
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                            "error", "Yêu cầu làm mới token",
                            "error_code", "token_required",
                            "message", "Valid session found, please refresh token"
                    ));
                } catch (IllegalArgumentException e) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                            "error", "Phiên không hợp lệ",
                            "error_code", "invalid_session",
                            "message", e.getMessage() != null ? e.getMessage() : "Invalid session"
                    ));
                }
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                    "error", "Chưa đăng nhập",
                    "error_code", "unauthenticated",
                    "message", "No valid JWT or session provided"
            ));
        }
        return ResponseEntity.ok(Map.of(
                "userId", jwt.getSubject(),
                "email", jwt.getClaim("email"),
                "roles", jwt.getClaim("roles")
        ));
    }
}
