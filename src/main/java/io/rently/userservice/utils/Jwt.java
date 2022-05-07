package io.rently.userservice.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.DefaultJwtSignatureValidator;
import io.rently.userservice.errors.Errors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.util.Date;
import java.util.UUID;

@Component
public class Jwt {

    private final SecretKeySpec secretKeySpec;
    private final DefaultJwtSignatureValidator validator;
    private final JwtParser parser;
    private final SignatureAlgorithm algo;

    public Jwt(@Value("${server.secret}") String secret, @Value("${server.algo}") SignatureAlgorithm algo) {
        Broadcaster.debug(secret);
        Broadcaster.debug(algo);
        this.algo = algo;
        this.secretKeySpec = new SecretKeySpec(secret.getBytes(), algo.getJcaName());
        this.validator = new DefaultJwtSignatureValidator(algo, secretKeySpec);
        this.parser = Jwts.parser().setSigningKey(secretKeySpec);
    }

    public boolean validateBearerToken(String token) {
        try {
            checkExpiration(token);
            String bearer = token.split(" ")[1];
            String[] chunks = bearer.split("\\.");
            String tokenWithoutSignature = chunks[0] + "." + chunks[1];
            String signature = chunks[2];
            return validator.isValid(tokenWithoutSignature, signature);
        } catch (Exception exception) {
            throw Errors.MALFORMED_TOKEN;
        }
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

    public String generateBearerToken() {
        String id = UUID.randomUUID().toString();
        Date iat = new Date();
        Date ext =  new Date(System.currentTimeMillis() + 60000L);

        return Jwts.builder()
                .setId(id)
                .setIssuedAt(iat)
                .setExpiration(ext)
                .signWith(algo, secretKeySpec)
                .compact();
    }
}
