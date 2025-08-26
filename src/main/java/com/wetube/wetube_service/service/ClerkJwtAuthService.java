
package com.wetube.wetube_service.service;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.JWSKeySelector;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import com.wetube.wetube_service.exception.InvalidClerkTokenException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;

import java.text.ParseException;

@Service
public class ClerkJwtAuthService {
    private final JWKSource<SecurityContext> keySource;
    private final String expectedIssuer;

    public ClerkJwtAuthService(JWKSource<SecurityContext> keySource, @Value("${clerk.jwks-url}") String jwksUrl) {
        this.keySource = keySource;
        this.expectedIssuer = URI.create(jwksUrl).getHost();
    }

    public JWTClaimsSet verifyClerkToken(String token) throws InvalidClerkTokenException {
        try {
            ConfigurableJWTProcessor<SecurityContext> jwtProcessor = new DefaultJWTProcessor<>();

            JWSKeySelector<SecurityContext> keySelector =
                    new JWSVerificationKeySelector<>(JWSAlgorithm.RS256, this.keySource);
            jwtProcessor.setJWSKeySelector(keySelector);

            JWTClaimsSet claimsSet = jwtProcessor.process(token, null);

            if (claimsSet.getIssuer() == null || !claimsSet.getIssuer().contains(this.expectedIssuer)) {
                throw new InvalidClerkTokenException("Invalid token issuer.");
            }

            return claimsSet;

        } catch (ParseException | com.nimbusds.jose.proc.BadJOSEException | com.nimbusds.jose.JOSEException e) {
            throw new InvalidClerkTokenException("Invalid Clerk token: " + e.getMessage(), e);
        }
    }
}
