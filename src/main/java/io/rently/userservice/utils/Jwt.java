package io.rently.userservice.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.crypto.DefaultJwtSignatureValidator;
import io.rently.userservice.errors.Errors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import javax.crypto.spec.SecretKeySpec;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Component
public class Jwt {

    private final JwtParser parser;
    private final SignatureAlgorithm algo;
    private final String secret;

    public Jwt(String secret, SignatureAlgorithm algo) {
        if (!Objects.equals(algo.getFamilyName(), "HMAC")) {
            throw new IllegalArgumentException("Algorithm outside of `HMAC` family: " + algo.getFamilyName());
        }
        if (secret == null || secret.equals("")) {
            throw new IllegalArgumentException("Signing secret cannot be null or an empty string");
        }
        this.secret = secret;
        this.algo = algo;
        this.parser = Jwts.parser().setSigningKey(secret);
    }

    public boolean validateBearerToken(String token) {
        try {
            String bearer = token.split(" ")[1];
            parser.parse(bearer);
        } catch (ExpiredJwtException exception) {
            throw Errors.EXPIRED_TOKEN;
        } catch (MalformedJwtException exception) {
            throw Errors.MALFORMED_TOKEN;
        } catch (Exception exception) {
            throw Errors.UNAUTHORIZED_REQUEST;
        }
        return true;
    }

    public String generateBearToken() {
        String id = UUID.randomUUID().toString();
        Date iat = new Date();
        Date ext = new Date(System.currentTimeMillis() + 60000L);

        return Jwts.builder()
                .setId(id)
                .setIssuedAt(iat)
                .setExpiration(ext)
                .signWith(algo, secret)
                .compact();
    }

    public JwtParser getParser() {
        return parser;
    }
}
