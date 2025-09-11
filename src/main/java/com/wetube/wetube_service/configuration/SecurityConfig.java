package com.wetube.wetube_service.configuration;

import com.wetube.wetube_service.security.ForbiddenEntryPoint;
import com.wetube.wetube_service.security.UnauthorizedEntryPoint;
import com.wetube.wetube_service.utility.KeyLoader;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.ArrayList;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {
    private final UnauthorizedEntryPoint unauthorizedEntryPoint;
    private final KeyLoader keyLoader;
    private final WebCsrfConfiguration webCsrfConfiguration;
    private final ForbiddenEntryPoint forbiddenEntryPoint;

    @Bean
    Converter<Jwt, JwtAuthenticationToken> authenticationConverter() {
        var roles = new JwtGrantedAuthoritiesConverter();
        roles.setAuthoritiesClaimName("roles"); // token nội bộ của bạn chứa "roles"
        roles.setAuthorityPrefix(""); // đã có tiền tố ROLE_ trong claim

        return jwt -> {
            var authorities = new ArrayList<>(roles.convert(jwt));
            return new JwtAuthenticationToken(jwt, authorities, jwt.getSubject());
        };
    }

    @Bean
    BearerTokenResolver bearerTokenResolver() {
        DefaultBearerTokenResolver delegate = new DefaultBearerTokenResolver();
        delegate.setAllowFormEncodedBodyParameter(false);
        delegate.setAllowUriQueryParameter(false);

        return request -> {
            String path = request.getRequestURI();
            // Ignore access token for endpoint refresh
            if (path.startsWith("/api/auth/refresh")) {
                return null;
            }
            return delegate.resolve(request);
        };
    }

    private static RequestMatcher authApiMatcher() {
        return request -> {
            String base = request.getContextPath(); // thường là ""
            String uri = request.getRequestURI();
            return uri.startsWith(base + "/api/auth/");
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, CorsConfigurationSource corsConfigurationSource) throws Exception {
        return http
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .csrf(csrf -> csrf
                        .csrfTokenRepository(webCsrfConfiguration.csrfTokenRepository())
                        .ignoringRequestMatchers(
                                webCsrfConfiguration.csrfIgnoringRequestMatcher(),
                                authApiMatcher() // ignore CSRF for /api/auth/**
                        ))
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(unauthorizedEntryPoint) // 401
                        .accessDeniedHandler(forbiddenEntryPoint) // 403
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2ResourceServer(oauth2 -> oauth2
                        .bearerTokenResolver(bearerTokenResolver())
                        .jwt(jwt -> jwt.decoder(NimbusJwtDecoder
                                .withPublicKey(keyLoader.loadPublicKey()).build())))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/users/**").permitAll()
                        .requestMatchers("/favicon.ico").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/me").authenticated()
                        .requestMatchers("/api/customers/**").authenticated())
                .build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
}
