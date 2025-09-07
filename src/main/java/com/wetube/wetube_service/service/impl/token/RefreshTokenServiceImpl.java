package com.wetube.wetube_service.service.impl.token;

import com.wetube.wetube_service.dto.IssueResult;
import com.wetube.wetube_service.dto.RotateResult;
import com.wetube.wetube_service.entity.AppUser;
import com.wetube.wetube_service.entity.RefreshToken;
import com.wetube.wetube_service.repository.RefreshTokenRepository;
import com.wetube.wetube_service.service.token.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${app.jwt.refresh-token-ttl-days}")
    private long ttlDays;

    private static String newRaw() {
        return UUID.randomUUID().toString() + "." + UUID.randomUUID();
    }

    private static String hash(String raw) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(raw.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hashBytes); // Trả về String
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error while hashing", e);
        }
    }

    @Override
    @Transactional
    public IssueResult issue(AppUser user, @Nullable String existingSid) {
        // Nếu refresh-login thì existingSid chính là SID từ cookie
        String sid = (existingSid != null && !existingSid.isBlank())
                ? existingSid
                : UUID.randomUUID().toString();

        // Khóa bản ghi theo session_id để tránh race
        RefreshToken rt = refreshTokenRepository.findByUserIdAndSessionIdForUpdate(user.getId(), sid)
                .orElseGet(() -> {
                    RefreshToken n = new RefreshToken();
                    n.setSessionId(sid);
                    n.setUser(user);
                    n.setCreatedAt(Instant.now());
                    return n;
                });

        String raw = newRaw();

        rt.setUser(user);
        rt.setSessionId(sid);
        rt.setTokenHash(hash(raw));
        rt.setParentTokenHash(null);
        rt.setRevoked(false);
        rt.setExpiresAt(Instant.now().plus(Duration.ofDays(ttlDays)));
        refreshTokenRepository.saveAndFlush(rt); // UPDATE nếu đã có, INSERT nếu chưa có

        return new IssueResult(sid, raw); // raw giữ ở server; client chỉ nhận SID cookie
    }

    @Override
    @Transactional(readOnly = true)
    public RefreshToken validateBySession(String sessionId) {
        return refreshTokenRepository.findBySessionIdAndRevokedFalse(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("invalid_session"));
    }

    @Override
    @Transactional
    public RotateResult rotateBySession(String sessionId) {
        RefreshToken current = refreshTokenRepository.findBySessionIdAndRevokedFalse(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("invalid_session"));

        if (current.getExpiresAt().isBefore(Instant.now())) {
            current.setRevoked(true);
            throw new IllegalArgumentException("session_expired");
        }

        // Single-use semantics cho refresh token: revoke cũ, tạo token mới cùng
        // sessionId
        current.setRevoked(true);

        String raw = newRaw();
        RefreshToken next = new RefreshToken();
        next.setUser(current.getUser());
        next.setSessionId(sessionId); // GIỮ nguyên SID (đỡ set cookie lại)
        next.setTokenHash(hash(raw));
        next.setParentTokenHash(current.getTokenHash());
        next.setRevoked(false);
        next.setExpiresAt(Instant.now().plus(Duration.ofDays(14)));
        refreshTokenRepository.save(next);

        return new RotateResult(sessionId, raw, next.getUser());
    }
}
