package io.rently.userservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.rently.userservice.dtos.User;
import io.rently.userservice.errors.Errors;
import io.rently.userservice.services.UserService;
import io.rently.userservice.utils.Jwt;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Date;
import java.util.UUID;

@WebMvcTest(UserController.class)
@ContextConfiguration(classes = UserControllerTestConfigs.class)
class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserService service;

    private static String validGenericJwt;

    @BeforeEach
    void generateNewJwt() {
        validGenericJwt = Jwts
                .builder()
                .setExpiration(new Date(System.currentTimeMillis()+100000))
                .signWith(SignatureAlgorithm.HS256, Jwt.SECRET_KEY_SPEC)
                .compact();
    }

    @AfterEach
    void reset_mocks() {
        Mockito.reset(service);
    }

    @Test
    void getUserByProvider_invalidProvider_notFound() throws Exception {
        String provider = "invalidProvider";
        String providerId = "providerId";

        Mockito.when(service.getUserByProvider(provider, providerId)).thenThrow(Errors.USER_NOT_FOUND);

        ResultActions response = mvc.perform(MockMvcRequestBuilders.get("/api/v2/{provider}/{providerId}", provider, providerId));

        String responseJson = response.andReturn().getResponse().getContentAsString();
        assert responseJson.contains(String.valueOf(Errors.USER_NOT_FOUND.getStatus().value()));
        assert responseJson.contains(String.valueOf(Errors.USER_NOT_FOUND.getReason()));
        response.andExpect(MockMvcResultMatchers.status().isNotFound());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.status").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.message").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.content").doesNotExist());
    }

    @Test
    void getUserByProvider_invalidProviderAccountId_notFound() throws Exception {
        String provider = "provider";
        String providerId = "invalidProviderId";

        Mockito.when(service.getUserByProvider(provider, providerId)).thenThrow(Errors.USER_NOT_FOUND);

        ResultActions response = mvc.perform(MockMvcRequestBuilders.get("/api/v2/{provider}/{providerId}", provider, providerId));

        String responseJson = response.andReturn().getResponse().getContentAsString();
        assert responseJson.contains(String.valueOf(Errors.USER_NOT_FOUND.getStatus().value()));
        assert responseJson.contains(String.valueOf(Errors.USER_NOT_FOUND.getReason()));
        response.andExpect(MockMvcResultMatchers.status().isNotFound());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.status").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.message").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.content").doesNotExist());
    }

    @Test
    void getUserByProvider_validProviderAndProviderAccountId_ok() throws Exception {
        String provider = "provider";
        String providerId = "providerId";
        User user = new User.Builder("abc123", providerId, provider).build();

        Mockito.when(service.getUserByProvider(provider, providerId)).thenReturn(user);

        ResultActions response = mvc.perform(MockMvcRequestBuilders.get("/api/v2/{provider}/{providerId}", provider, providerId));

        String responseJson = response.andReturn().getResponse().getContentAsString();
        assert responseJson.contains(String.valueOf(HttpStatus.OK.value()));
        assert responseJson.contains(provider);
        assert responseJson.contains(providerId);
        response.andExpect(MockMvcResultMatchers.status().isOk());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.status").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.message").doesNotExist());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.content").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.content.id").isNotEmpty());
    }

    @Test
    void getUserById_invalidUserId_notFound() throws Exception {
        String userId = "invalidUserId";

        Mockito.when(service.getUserById(userId)).thenThrow(Errors.USER_NOT_FOUND);

        ResultActions response = mvc.perform(MockMvcRequestBuilders.get("/api/v2/{id}", userId));

        String responseJson = response.andReturn().getResponse().getContentAsString();
        assert responseJson.contains(String.valueOf(Errors.USER_NOT_FOUND.getStatus().value()));
        assert responseJson.contains(String.valueOf(Errors.USER_NOT_FOUND.getReason()));
        response.andExpect(MockMvcResultMatchers.status().isNotFound());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.status").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.message").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.content").doesNotExist());
    }

    @Test
    void getUserById_validUserId_ok() throws Exception {
        String userId = "abc123";
        User user = new User.Builder(userId, "providerId", "provider").build();

        Mockito.when(service.getUserById(userId)).thenReturn(user);

        ResultActions response = mvc.perform(MockMvcRequestBuilders.get("/api/v2/{id}", userId));

        String responseJson = response.andReturn().getResponse().getContentAsString();
        assert responseJson.contains(String.valueOf(HttpStatus.OK.value()));
        assert responseJson.contains(userId);
        response.andExpect(MockMvcResultMatchers.status().isOk());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.status").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.message").doesNotExist());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.content").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.content.id").isNotEmpty());
    }

    @Test
    void postUser_noJwt_badRequest() throws Exception {
        ResultActions response = mvc.perform(MockMvcRequestBuilders.post("/api/v2/"));

        String responseJson = response.andReturn().getResponse().getContentAsString();
        assert responseJson.contains(String.valueOf(Errors.INVALID_REQUEST.getStatus().value()));
        assert responseJson.contains(String.valueOf(Errors.INVALID_REQUEST.getReason()));
        response.andExpect(MockMvcResultMatchers.status().isBadRequest());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.status").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.message").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.content").doesNotExist());
    }

    @Test
    void postUser_outdatedRequestJwt_notAuthorized() throws Exception {
        String jwt = Jwts.builder()
                .setExpiration(new Date(System.currentTimeMillis()-1000))
                .signWith(SignatureAlgorithm.HS256, Jwt.SECRET_KEY_SPEC)
                .compact();

        ResultActions response = mvc.perform(MockMvcRequestBuilders
                .post("/api/v2/")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt));

        String responseJson = response.andReturn().getResponse().getContentAsString();
        assert responseJson.contains(String.valueOf(Errors.UNAUTHORIZED_REQUEST.getStatus().value()));
        assert responseJson.contains(String.valueOf(Errors.UNAUTHORIZED_REQUEST.getReason()));
        response.andExpect(MockMvcResultMatchers.status().isUnauthorized());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.status").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.message").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.content").doesNotExist());
    }

    @Test
    void postUser_noBody_notAcceptable() throws Exception {
        ResultActions response = mvc.perform(MockMvcRequestBuilders
                .post("/api/v2/")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + validGenericJwt));

        String responseJson = response.andReturn().getResponse().getContentAsString();
        assert responseJson.contains(String.valueOf(Errors.NO_DATA.getStatus().value()));
        assert responseJson.contains(String.valueOf(Errors.NO_DATA.getReason()));
        response.andExpect(MockMvcResultMatchers.status().isNotAcceptable());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.status").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.message").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.content").doesNotExist());
    }

    @Test
    void postUser_userAlreadyExists_conflict() throws Exception {
        String provider = "provider";
        String providerId = "providerId";
        User newUser = new User
                .Builder("def456", providerId, provider)
                .build();
        String jsonBody = new ObjectMapper()
                .writeValueAsString(newUser);

        Mockito.doThrow(Errors.USER_ALREADY_EXISTS)
                .when(service)
                .addUser(Mockito.any(newUser.getClass()));

        ResultActions response = mvc.perform(MockMvcRequestBuilders
                .post("/api/v2/")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + validGenericJwt)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON));

        String responseJson = response.andReturn().getResponse().getContentAsString();
        assert responseJson.contains(String.valueOf(Errors.USER_ALREADY_EXISTS.getStatus().value()));
        assert responseJson.contains(String.valueOf(Errors.USER_ALREADY_EXISTS.getReason()));
        response.andExpect(MockMvcResultMatchers.status().isConflict());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.status").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.message").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.content").doesNotExist());
    }

    @Test
    void postUser_validationFailure_notAcceptable() throws Exception {
        String id = "notUuidId";
        User newUser = new User
                .Builder(id, "provider", "providerAccountId")
                .build();
        String jsonBody = new ObjectMapper()
                .writeValueAsString(newUser);

        Mockito.doThrow(new Errors.HttpValidationFailure("id", UUID.class, newUser.getId()))
                .when(service)
                .addUser(Mockito.any(newUser.getClass()));

        ResultActions response = mvc.perform(MockMvcRequestBuilders
                .post("/api/v2/")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + validGenericJwt)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON));

        String responseJson = response.andReturn().getResponse().getContentAsString();
        assert responseJson.contains(String.valueOf(HttpStatus.NOT_ACCEPTABLE.value()));
        assert responseJson.contains(String.valueOf(HttpStatus.NOT_ACCEPTABLE.value()));
        response.andExpect(MockMvcResultMatchers.status().isNotAcceptable());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.status").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.message").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.content").doesNotExist());
    }

    @Test
    void postUser_missingField_notAcceptable() throws Exception {
        User newUser = new User
                .Builder(null, "provider", "providerAccountId")
                .build();
        String jsonBody = new ObjectMapper()
                .writeValueAsString(newUser);

        Mockito.doThrow(new Errors.HttpFieldMissing("id"))
                .when(service)
                .addUser(Mockito.any(newUser.getClass()));

        ResultActions response = mvc.perform(MockMvcRequestBuilders
                .post("/api/v2/")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + validGenericJwt)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON));

        String responseJson = response.andReturn().getResponse().getContentAsString();
        assert responseJson.contains(String.valueOf(HttpStatus.NOT_ACCEPTABLE.value()));
        assert responseJson.contains(String.valueOf(HttpStatus.NOT_ACCEPTABLE.value()));
        response.andExpect(MockMvcResultMatchers.status().isNotAcceptable());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.status").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.message").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.content").doesNotExist());
    }

    @Test
    void postUser_validBody_validRequest_created() throws Exception {
        User newUser = new User
                .Builder("id", "provider", "providerAccountId")
                .build();
        String jsonBody = new ObjectMapper().writeValueAsString(newUser);

        ResultActions response = mvc.perform(
                MockMvcRequestBuilders
                        .post("/api/v2/")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + validGenericJwt)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON));

        String responseJson = response.andReturn().getResponse().getContentAsString();
        assert responseJson.contains(String.valueOf(HttpStatus.CREATED.value()));
        response.andExpect(MockMvcResultMatchers.status().isCreated());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.status").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.message").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.content").doesNotExist());
    }

    @Test
    void putUser_outdatedRequestJwt_notAuthorized() throws Exception {
        String jwt = Jwts.builder()
                .setExpiration(new Date(System.currentTimeMillis()-1000))
                .signWith(SignatureAlgorithm.HS256, Jwt.SECRET_KEY_SPEC)
                .compact();

        ResultActions response = mvc.perform(MockMvcRequestBuilders
                .put("/api/v2/{id}", "abc")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt));

        String responseJson = response.andReturn().getResponse().getContentAsString();
        assert responseJson.contains(String.valueOf(Errors.UNAUTHORIZED_REQUEST.getStatus().value()));
        assert responseJson.contains(String.valueOf(Errors.UNAUTHORIZED_REQUEST.getReason()));
        response.andExpect(MockMvcResultMatchers.status().isUnauthorized());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.status").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.message").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.content").doesNotExist());
    }

    @Test
    void putUser_noJwt_badRequest() throws Exception {
        ResultActions response = mvc.perform(MockMvcRequestBuilders.put("/api/v2/{id}", "abc"));

        String responseJson = response.andReturn().getResponse().getContentAsString();
        assert responseJson.contains(String.valueOf(Errors.INVALID_REQUEST.getStatus().value()));
        assert responseJson.contains(String.valueOf(Errors.INVALID_REQUEST.getReason()));
        response.andExpect(MockMvcResultMatchers.status().isBadRequest());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.status").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.message").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.content").doesNotExist());
    }

    @Test
    void putUser_invalidJwtSubject_notAuthorized() throws Exception {
        String id = "userId";
        String subject = "invalidUserId";
        User userData = new User
                .Builder(id, "providerId", "provider")
                .build();
        String jsonBody = new ObjectMapper()
                .writeValueAsString(userData);
        String jwt = Jwts.builder()
                .setSubject(subject)
                .setExpiration(new Date(System.currentTimeMillis()+10000))
                .signWith(SignatureAlgorithm.HS256, Jwt.SECRET_KEY_SPEC)
                .compact();

        Mockito.doThrow(Errors.UNAUTHORIZED_REQUEST)
                .when(service)
                .verifyOwnership(Mockito.any(subject.getClass()), Mockito.any(userData.getClass()));

        ResultActions response = mvc.perform(MockMvcRequestBuilders
                .put("/api/v2/{id}", id)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON));

        String responseJson = response.andReturn().getResponse().getContentAsString();
        assert responseJson.contains(String.valueOf(Errors.UNAUTHORIZED_REQUEST.getStatus().value()));
        assert responseJson.contains(String.valueOf(Errors.UNAUTHORIZED_REQUEST.getReason()));
        response.andExpect(MockMvcResultMatchers.status().isUnauthorized());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.status").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.message").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.content").doesNotExist());
    }

    @Test
    void putUser_invalidUserId_notFound() throws Exception {
        String userId = "invalidUserId";
        User userData = new User
                .Builder(userId, "providerId", "provider")
                .build();
        String jsonBody = new ObjectMapper()
                .writeValueAsString(userData);

        Mockito.doThrow(Errors.USER_NOT_FOUND)
                .when(service)
                .updateUser(Mockito.any(userId.getClass()), Mockito.any(userData.getClass()));

        ResultActions response = mvc.perform(MockMvcRequestBuilders
                .put("/api/v2/{id}", userId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + validGenericJwt)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON));

        String responseJson = response.andReturn().getResponse().getContentAsString();
        assert responseJson.contains(String.valueOf(Errors.USER_NOT_FOUND.getStatus().value()));
        assert responseJson.contains(String.valueOf(Errors.USER_NOT_FOUND.getReason()));
        response.andExpect(MockMvcResultMatchers.status().isNotFound());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.status").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.message").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.content").doesNotExist());
    }

    @Test
    void putUser_conflictingBodyIdAndPathId_badRequest() throws Exception {
        String userId = "invalidUserId";
        User userData = new User
                .Builder(userId, "providerId", "provider")
                .build();
        String jsonBody = new ObjectMapper()
                .writeValueAsString(userData);

        Mockito.doThrow(Errors.INVALID_REQUEST)
                .when(service)
                .updateUser(Mockito.any(userId.getClass()), Mockito.any(userData.getClass()));

        ResultActions response = mvc.perform(MockMvcRequestBuilders
                .put("/api/v2/{id}", userId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + validGenericJwt)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON));

        String responseJson = response.andReturn().getResponse().getContentAsString();
        assert responseJson.contains(String.valueOf(Errors.INVALID_REQUEST.getStatus().value()));
        assert responseJson.contains(String.valueOf(Errors.INVALID_REQUEST.getReason()));
        response.andExpect(MockMvcResultMatchers.status().isBadRequest());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.status").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.message").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.content").doesNotExist());
    }

    @Test
    void putUser_noBody_notAcceptable() throws Exception {
        ResultActions response = mvc.perform(MockMvcRequestBuilders
                .put("/api/v2/{id}", "abc")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + validGenericJwt));

        String responseJson = response.andReturn().getResponse().getContentAsString();
        assert responseJson.contains(String.valueOf(Errors.NO_DATA.getStatus().value()));
        assert responseJson.contains(String.valueOf(Errors.NO_DATA.getReason()));
        response.andExpect(MockMvcResultMatchers.status().isNotAcceptable());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.status").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.message").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.content").doesNotExist());
    }

    @Test
    void putUser_validationFailure_notAcceptable() throws Exception {
        String id = "notUuidId";
        User newUser = new User
                .Builder(id, "provider", "providerAccountId")
                .build();
        String jsonBody = new ObjectMapper()
                .writeValueAsString(newUser);

        Mockito.doThrow(new Errors.HttpValidationFailure("id", UUID.class, newUser.getId()))
                .when(service)
                .updateUser(Mockito.any(id.getClass()), Mockito.any(newUser.getClass()));

        ResultActions response = mvc.perform(MockMvcRequestBuilders
                .put("/api/v2/{id}", id)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + validGenericJwt)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON));

        String responseJson = response.andReturn().getResponse().getContentAsString();
        assert responseJson.contains(String.valueOf(HttpStatus.NOT_ACCEPTABLE.value()));
        assert responseJson.contains(String.valueOf(HttpStatus.NOT_ACCEPTABLE.value()));
        response.andExpect(MockMvcResultMatchers.status().isNotAcceptable());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.status").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.message").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.content").doesNotExist());
    }

    @Test
    void putUser_missingField_notAcceptable() throws Exception {
        String id = "id";
        User newUser = new User
                .Builder(id, "provider", "providerAccountId")
                .build();
        String jsonBody = new ObjectMapper()
                .writeValueAsString(newUser);

        Mockito.doThrow(new Errors.HttpFieldMissing("id"))
                .when(service)
                .updateUser(Mockito.any(id.getClass()), Mockito.any(newUser.getClass()));

        ResultActions response = mvc.perform(MockMvcRequestBuilders
                .put("/api/v2/{id}", id)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + validGenericJwt)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON));

        String responseJson = response.andReturn().getResponse().getContentAsString();
        assert responseJson.contains(String.valueOf(HttpStatus.NOT_ACCEPTABLE.value()));
        assert responseJson.contains(String.valueOf(HttpStatus.NOT_ACCEPTABLE.value()));
        response.andExpect(MockMvcResultMatchers.status().isNotAcceptable());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.status").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.message").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.content").doesNotExist());
    }

    @Test
    void putUser_validBody_validRequest_ok() throws Exception {
        String id = "abc123";
        User userData = new User
                .Builder(id, "provider", "providerAccountId")
                .build();
        String jsonBody = new ObjectMapper()
                .writeValueAsString(userData);

        ResultActions response = mvc.perform(MockMvcRequestBuilders
                .put("/api/v2/{id}", id)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + validGenericJwt)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON));

        String responseJson = response.andReturn().getResponse().getContentAsString();
        assert responseJson.contains(String.valueOf(HttpStatus.OK.value()));
        response.andExpect(MockMvcResultMatchers.status().isOk());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.status").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.message").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.content").doesNotExist());
    }

    @Test
    void deleteUser_noJwt_badRequest() throws Exception {
        ResultActions response = mvc.perform(MockMvcRequestBuilders
                .delete("/api/v2/{id}", "abc"));

        String responseJson = response.andReturn().getResponse().getContentAsString();
        assert responseJson.contains(String.valueOf(Errors.INVALID_REQUEST.getStatus().value()));
        assert responseJson.contains(String.valueOf(Errors.INVALID_REQUEST.getReason()));
        response.andExpect(MockMvcResultMatchers.status().isBadRequest());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.status").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.message").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.content").doesNotExist());
    }

    @Test
    void deleteUser_outdatedRequestJwt_notAuthorized() throws Exception {
        String jwt = Jwts.builder()
                .setExpiration(new Date(System.currentTimeMillis()-1000))
                .signWith(SignatureAlgorithm.HS256, Jwt.SECRET_KEY_SPEC)
                .compact();

        ResultActions response = mvc.perform(MockMvcRequestBuilders
                .delete("/api/v2/{id}", "abc")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt));

        String responseJson = response.andReturn().getResponse().getContentAsString();
        assert responseJson.contains(String.valueOf(Errors.UNAUTHORIZED_REQUEST.getStatus().value()));
        assert responseJson.contains(String.valueOf(Errors.UNAUTHORIZED_REQUEST.getReason()));
        response.andExpect(MockMvcResultMatchers.status().isUnauthorized());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.status").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.message").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.content").doesNotExist());
    }

    @Test
    void deleteUser_invalidJwtSubject_notAuthorized() throws Exception {
        String id = "userId";
        String subject = "invalidUserId";
        String jwt = Jwts.builder()
                .setSubject(subject)
                .setExpiration(new Date(System.currentTimeMillis()+10000))
                .signWith(SignatureAlgorithm.HS256, Jwt.SECRET_KEY_SPEC)
                .compact();

        Mockito.doThrow(Errors.UNAUTHORIZED_REQUEST)
                .when(service)
                .verifyOwnership(Mockito.any(subject.getClass()), Mockito.any(id.getClass()));

        ResultActions response = mvc.perform(MockMvcRequestBuilders
                .delete("/api/v2/{id}", id)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt));

        String responseJson = response.andReturn().getResponse().getContentAsString();
        assert responseJson.contains(String.valueOf(Errors.UNAUTHORIZED_REQUEST.getStatus().value()));
        assert responseJson.contains(String.valueOf(Errors.UNAUTHORIZED_REQUEST.getReason()));
        response.andExpect(MockMvcResultMatchers.status().isUnauthorized());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.status").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.message").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.content").doesNotExist());
    }

    @Test
    void deleteUser_invalidUserId_notFound() throws Exception {
        String userId = "invalidUserId";

        Mockito.doThrow(Errors.USER_NOT_FOUND)
                .when(service)
                .deleteUser(Mockito.any(userId.getClass()));

        ResultActions response = mvc.perform(MockMvcRequestBuilders
                .delete("/api/v2/{id}", userId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + validGenericJwt));

        String responseJson = response.andReturn().getResponse().getContentAsString();
        assert responseJson.contains(String.valueOf(Errors.USER_NOT_FOUND.getStatus().value()));
        assert responseJson.contains(String.valueOf(Errors.USER_NOT_FOUND.getReason()));
        response.andExpect(MockMvcResultMatchers.status().isNotFound());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.status").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.message").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.content").doesNotExist());
    }

    @Test
    void deleteUser_validRequest_ok() throws Exception {
        String id = "abc123";

        ResultActions response = mvc.perform(MockMvcRequestBuilders
                .delete("/api/v2/{id}", id)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + validGenericJwt));

        String responseJson = response.andReturn().getResponse().getContentAsString();
        assert responseJson.contains(String.valueOf(HttpStatus.OK.value()));
        response.andExpect(MockMvcResultMatchers.status().isOk());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.status").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.message").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.content").doesNotExist());
    }
}