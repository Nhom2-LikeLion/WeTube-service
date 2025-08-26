package com.wetube.wetube_service.security;

import com.nimbusds.jwt.JWTClaimsSet;
import com.wetube.wetube_service.service.ClerkJwtAuthService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import java.io.IOException;
import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final ClerkJwtAuthService clerkAuthService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        // Nếu không có token hoặc không phải Bearer token, cho qua để các filter khác xử lý
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = authHeader.substring(7);

        try {
            // 1. Dùng service cũ để xác thực token
            JWTClaimsSet claims = clerkAuthService.verifyClerkToken(jwt);
            String clerkId = claims.getSubject();

            // 2. Nếu token hợp lệ và chưa có ai được xác thực
            if (clerkId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // 3. ✅ Tạo một đối tượng "xác thực" cho Spring Security
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
//                        clerkId, // Principal chính là Clerk User ID
                        claims,
                        null,
                        new ArrayList<>() // authorities (quyền hạn), có thể lấy từ DB sau
                );

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 4. ✅ Đặt đối tượng này vào Security Context
                // Từ giờ, Spring Security sẽ biết người dùng này đã được xác thực
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        } catch (Exception e) {
            // Nếu token không hợp lệ, không làm gì cả.
            // SecurityContext sẽ vẫn trống và yêu cầu sẽ bị từ chối ở bước sau.
        }

        // Chuyển yêu cầu đi tiếp chuỗi filter
        filterChain.doFilter(request, response);
    }
}