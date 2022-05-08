package io.rently.userservice.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.rently.userservice.errors.Errors;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertThrows;

class JwtTest {

    public static final String SECRET = "secret";
    public static final SignatureAlgorithm ALGORITHM = SignatureAlgorithm.HS384;

    @Test
    void invalidAlgo_agrException() {
        SignatureAlgorithm jwtAlgo = SignatureAlgorithm.ES256;

        assertThrows(IllegalArgumentException.class, () -> new Jwt(SECRET, jwtAlgo));
    }

    @Test
    void invalidSecret_agrException() {
        String jwtSecret = "";

        assertThrows(IllegalArgumentException.class, () -> new Jwt(jwtSecret, ALGORITHM));
    }

    @Test
    void generateBearerToken() {
        Jwt jwt = new Jwt(SECRET, ALGORITHM);

        String token = jwt.generateBearToken();

        assert token.matches("(^[A-Za-z0-9-_]*\\.[A-Za-z0-9-_]*\\.[A-Za-z0-9-_]*$)");
    }

    @Test
    void validateBearerToken_malformedToken_malformedException() {
        Jwt jwt = new Jwt(SECRET, ALGORITHM);
        String token = "malformed token";

        assertThrows(Errors.MALFORMED_TOKEN.getClass(), () -> jwt.validateBearerToken(token));
    }

    @Test
    void validateBearerToken_invalidSecret_unauthorizedException() {
        Jwt jwt = new Jwt(SECRET, ALGORITHM);
        Date validDate = new Date(System.currentTimeMillis() - 60000L);
        String invalidSecret = "invalid secret";
        String token = Jwts.builder()
                .setIssuedAt(validDate)
                .setExpiration(validDate)
                .signWith(ALGORITHM, invalidSecret)
                .compact();
        String bearer = "Bearer " + token;

        assertThrows(Errors.UNAUTHORIZED_REQUEST.getClass(), () -> jwt.validateBearerToken(bearer));
    }

    @Test
    void validateBearerToken_invalidAlgo_unauthorizedException() {
        Jwt jwt = new Jwt(SECRET, ALGORITHM);
        Date validDate = new Date(System.currentTimeMillis() - 60000L);
        SignatureAlgorithm invalidAlgo = SignatureAlgorithm.HS384;
        String token = Jwts.builder()
                .setIssuedAt(validDate)
                .setExpiration(validDate)
                .signWith(invalidAlgo, SECRET)
                .compact();
        String bearer = "Bearer " + token;

        assertThrows(Errors.UNAUTHORIZED_REQUEST.getClass(), () -> jwt.validateBearerToken(bearer));
    }

    @Test
    void validateBearerToken_expiredToken_tokenExpiredException() {
        Jwt jwt = new Jwt(SECRET, ALGORITHM);
        Date pastDate = new Date(System.currentTimeMillis() - 60000L);
        String token = Jwts.builder()
                .setIssuedAt(pastDate)
                .setExpiration(pastDate)
                .signWith(ALGORITHM, SECRET)
                .compact();
        String bearer = "Bearer " + token;

        assertThrows(Errors.EXPIRED_TOKEN.getClass(), () -> jwt.validateBearerToken(bearer));
    }

    @Test
    void validateBearerToken_validToken() {
        Jwt jwt = new Jwt(SECRET, ALGORITHM);
        Date validDate = new Date(System.currentTimeMillis() + 60000L);
        String token = Jwts.builder()
                .setIssuedAt(validDate)
                .setExpiration(validDate)
                .signWith(ALGORITHM, SECRET)
                .compact();
        String bearer = "Bearer " + token;

        assert jwt.validateBearerToken(bearer);
    }

    @Test
    void getParser() {
        Jwt jwt = new Jwt(SECRET, ALGORITHM);

        assert jwt.getParser() != null;
    }
}