package com.wetube.wetube_service.configuration;

import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.jwk.source.JWKSourceBuilder;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.MalformedURLException;
import java.net.URI;

@Configuration
public class JwtConfig {
    @Bean
    public JWKSource<SecurityContext> jwkSource(@Value("${clerk.jwks-url}") String jwksUrl) throws MalformedURLException {
        long cacheLifeSpanMillis = java.util.concurrent.TimeUnit.MINUTES.toMillis(15);
        long cacheRefreshMillis = java.util.concurrent.TimeUnit.MINUTES.toMillis(5);

        return JWKSourceBuilder.create(URI.create(jwksUrl).toURL())
                .retrying(true)
                .cache(cacheLifeSpanMillis, cacheRefreshMillis)
                .rateLimited(true)
                .build();
    }
}
