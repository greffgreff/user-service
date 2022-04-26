package io.rently.userservice.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.DefaultJwtSignatureValidator;
import io.rently.userservice.errors.Errors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.util.Date;

@Component
public class Jwt {
    public final DefaultJwtSignatureValidator validator;
    public final JwtParser parser;
    public final SecretKeySpec secretKeySpec;

    @Autowired
    public Jwt(@Value("${server.secret}") String secret) {
        this(secret, SignatureAlgorithm.HS256);
    }

    public Jwt(String secret, SignatureAlgorithm algo) {
        this.secretKeySpec = new SecretKeySpec(secret.getBytes(), algo.getJcaName());
        this.validator = new DefaultJwtSignatureValidator(algo, secretKeySpec);
        this.parser = Jwts.parser().setSigningKey(secretKeySpec);
    }

    public boolean validateBearerToken(String token) {
        checkExpiration(token);
        String bearer = token.split(" ")[1];
        String[] chunks = bearer.split("\\.");
        String tokenWithoutSignature = chunks[0] + "." + chunks[1];
        String signature = chunks[2];
        return validator.isValid(tokenWithoutSignature, signature);
    }

    public void checkExpiration(String token) {
        try {
            getClaims(token);
        } catch (Exception e) {
            throw Errors.UNAUTHORIZED_REQUEST;
        }
    }

    public Claims getClaims(String token) {
        String bearer = token.split(" ")[1];
        return parser.parseClaimsJws(bearer).getBody();
    }
}
