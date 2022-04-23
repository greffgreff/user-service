package io.rently.userservice.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.DefaultJwtSignatureValidator;
import io.rently.userservice.errors.Errors;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.spec.SecretKeySpec;
import java.util.Date;

public class Jwt {
    public static SignatureAlgorithm ALGO = SignatureAlgorithm.HS256;
    public static SecretKeySpec SECRET_KEY_SPEC;
    public static DefaultJwtSignatureValidator VALIDATOR;
    public static JwtParser PARSER;

    @Value("${server.secret}")
    public void setSecret(String secret) {
        SECRET_KEY_SPEC = new SecretKeySpec(secret.getBytes(), ALGO.getJcaName());
        VALIDATOR = new DefaultJwtSignatureValidator(ALGO, SECRET_KEY_SPEC);
        PARSER = Jwts.parser().setSigningKey(SECRET_KEY_SPEC);
    }

    public static boolean validateBearerToken(String token) {
        checkExpiration(token);
        String bearer = token.split(" ")[1];
        String[] chunks = bearer.split("\\.");
        String tokenWithoutSignature = chunks[0] + "." + chunks[1];
        String signature = chunks[2];
        return VALIDATOR.isValid(tokenWithoutSignature, signature);
    }

    public static void checkExpiration(String token) {
        try {
            getClaims(token);
        } catch (Exception e) {
            throw Errors.UNAUTHORIZED_REQUEST;
        }
    }

    public static Claims getClaims(String token) {
        String bearer = token.split(" ")[1];
        return PARSER.parseClaimsJws(bearer).getBody();
    }
}
