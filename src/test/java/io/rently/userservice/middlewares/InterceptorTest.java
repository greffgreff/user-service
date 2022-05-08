package io.rently.userservice.middlewares;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.rently.userservice.errors.Errors;
import io.rently.userservice.utils.Jwt;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartHttpServletRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Objects;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
class InterceptorTest {

    public Interceptor interceptor;
    public MockHttpServletResponse response;
    public MockMultipartHttpServletRequest request;
    public static final String SECRET = "secret";
    public static final SignatureAlgorithm ALGORITHM = SignatureAlgorithm.HS384;

    @BeforeEach
    void setup() {
        interceptor = new Interceptor(new Jwt(SECRET, ALGORITHM));
        request = new MockMultipartHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    void preHandle_noJwt_unauthorizedRequestThrown() {
        Assertions.assertThrows(Errors.UNAUTHORIZED_REQUEST.getClass(), () -> interceptor.preHandle(request, response, new Object()));
    }

    @Test
    void preHandle_malFormedJwt_malformedRequestThrown() {
        request.addHeader("Authorization", "Bearer abc");

        Assertions.assertThrows(Errors.MALFORMED_TOKEN.getClass(), () -> interceptor.preHandle(request, response, new Object()));
    }

    @Test
    void preHandle_expiredJwt_expiredRequestThrown() {
        Date expiredDate = new Date(System.currentTimeMillis() - 60000L);
        String token = Jwts.builder()
                .setIssuedAt(expiredDate)
                .setExpiration(expiredDate)
                .signWith(ALGORITHM, SECRET)
                .compact();

        request.addHeader("Authorization", "Bearer " + token);

        Assertions.assertThrows(Errors.EXPIRED_TOKEN.getClass(), () -> interceptor.preHandle(request, response, new Object()));
    }

    @Test
    void preHandle_validJwt_returnsTrue() {
        Date expiredDate = new Date(System.currentTimeMillis() + 60000L);
        String token = Jwts.builder()
                .setIssuedAt(expiredDate)
                .setExpiration(expiredDate)
                .signWith(ALGORITHM, SECRET)
                .compact();

        request.addHeader("Authorization", "Bearer " + token);

        assert interceptor.preHandle(request, response, new Object());
    }

    @Test
    void preHandle_whenRequestMethodIsOption_returnTrue() {
        request.setMethod(RequestMethod.OPTIONS.name());

        assert interceptor.preHandle(request, response, new Object());
        assert Objects.equals(response.getHeader("Cache-Control"), "no-cache");
        assert Objects.equals(response.getHeader("Access-control-Allow-Origin"), "*");
        assert Objects.equals(response.getHeader("Access-Control-Allow-Methods"), "GET,POST,OPTIONS,PUT,DELETE");
        assert Objects.equals(response.getHeader("Access-Control-Allow-Headers"), "*");
        assert response.getStatus() == HttpServletResponse.SC_OK;
    }

    @Test
    void preHandle_whenRequestMethodIsExcluded_returnTrue() {
        Interceptor interceptor = new Interceptor(new Jwt(SECRET, ALGORITHM), RequestMethod.GET);

        request.setMethod(RequestMethod.GET.name());

        assert interceptor.preHandle(request, response, new Object());
    }
}