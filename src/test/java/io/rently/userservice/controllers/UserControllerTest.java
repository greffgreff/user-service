package io.rently.userservice.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.rently.userservice.configs.UserControllerTestConfigs;
import io.rently.userservice.dtos.User;
import io.rently.userservice.interfaces.UserRepository;
import io.rently.userservice.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.crypto.spec.SecretKeySpec;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = UserControllerTestConfigs.class)
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude = {
        DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class})
class UserControllerTest {

    @Autowired
    public MockMvc mvc;
    @Autowired
    public UserRepository repository;
    public static final String SECRET = "HelloDarknessMyOldFriend";
    public static final SignatureAlgorithm ALGORITHM = SignatureAlgorithm.HS384;
    public static final SecretKeySpec SECRET_KEY_SPEC = new SecretKeySpec(SECRET.getBytes(), ALGORITHM.getJcaName());
    public String token;
    public String validUserId = UUID.randomUUID().toString();
    public User validUserData;

    @BeforeEach
    public void generateValidJwt() {
        Date expiredDate = new Date(System.currentTimeMillis() + 60000L);
        token = Jwts.builder()
                .setIssuedAt(expiredDate)
                .setExpiration(expiredDate)
                .setSubject(validUserId)
                .signWith(ALGORITHM, SECRET_KEY_SPEC)
                .compact();

        validUserData = new User();
        validUserData.setId(validUserId);
        validUserData.setName("name");
        validUserData.setEmail("email");
        validUserData.setCreatedAt(String.valueOf(new Date().getTime()));
        validUserData.setUpdatedAt(String.valueOf(new Date().getTime()));
        validUserData.setProviderId("123123123");
        validUserData.setProvider("discord");
    }

    @Test
    void handleGetRequest_validUserId_returnUser() throws Exception {
        doReturn(Optional.of(validUserData)).when(repository).findById(validUserId);

        ResultActions response = mvc.perform(get("/api/v2/users/{id}", validUserId));

        String responseJson = response.andReturn().getResponse().getContentAsString();
        response.andExpect(MockMvcResultMatchers.status().isOk());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.status").isNotEmpty());
        assert responseJson.contains(String.valueOf(HttpStatus.OK.value()));
        response.andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.message").doesNotExist());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.content").isNotEmpty());
        assert responseJson.contains(validUserId);
    }

    @Test
    void handleGetRequest_validProviderInfo_returnUser() throws Exception {
        String validProviderId = "123213123123";
        String validProvider = "discord";
        User user = new User();
        user.setProvider(validProvider);
        user.setProviderId(validProviderId);

        doReturn(Optional.of(user)).when(repository).findByProviderAndProviderId(validProvider, validProviderId);

        ResultActions response = mvc.perform(get("/api/v2/users/{provider}/{providerId}", validProvider, validProviderId));

        String responseJson = response.andReturn().getResponse().getContentAsString();
        response.andExpect(MockMvcResultMatchers.status().isOk());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.status").isNotEmpty());
        assert responseJson.contains(String.valueOf(HttpStatus.OK.value()));
        response.andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.message").doesNotExist());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.content").isNotEmpty());
        assert responseJson.contains(validProvider);
        assert responseJson.contains(validProviderId);
    }

    @Test
    void handlePostRequest_mismatchedDataHolderIds_throwUnauthorized() throws Exception {
        validUserData.setId("invalidId");
        String jsonBody = new ObjectMapper().writeValueAsString(validUserData);

        ResultActions response = mvc.perform(post("/api/v2/users/")
                .content(jsonBody).contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token));

        String responseJson = response.andReturn().getResponse().getContentAsString();
        response.andExpect(MockMvcResultMatchers.status().isUnauthorized());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.status").isNotEmpty());
        assert responseJson.contains(String.valueOf(HttpStatus.UNAUTHORIZED.value()));
        response.andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.message").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.content").doesNotExist());
    }

    @Test
    void handlePostRequest_validUserData_successMsg() throws Exception {
        String jsonBody = new ObjectMapper().writeValueAsString(validUserData);

        ResultActions response = mvc.perform(post("/api/v2/users/")
                .content(jsonBody).contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token));

        String responseJson = response.andReturn().getResponse().getContentAsString();
        response.andExpect(MockMvcResultMatchers.status().isCreated());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.status").isNotEmpty());
        assert responseJson.contains(String.valueOf(HttpStatus.CREATED.value()));
        response.andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.message").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.content").doesNotExist());
    }

    @Test
    void handlePutRequest_mismatchedDataHolderIds_throwUnauthorized() throws Exception {
        validUserData.setId("invalidId");
        String jsonBody = new ObjectMapper().writeValueAsString(validUserData);

        ResultActions response = mvc.perform(put("/api/v2/users/{id}", validUserId)
                .content(jsonBody).contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token));

        String responseJson = response.andReturn().getResponse().getContentAsString();
        response.andExpect(MockMvcResultMatchers.status().isUnauthorized());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.status").isNotEmpty());
        assert responseJson.contains(String.valueOf(HttpStatus.UNAUTHORIZED.value()));
        response.andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.message").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.content").doesNotExist());
    }

    @Test
    void handlePutRequest_validUserData_successMsg() throws Exception {
        String jsonBody = new ObjectMapper().writeValueAsString(validUserData);

        doReturn(Optional.of(validUserData)).when(repository).findById(validUserId);

        ResultActions response = mvc.perform(put("/api/v2/users/{id}", validUserId)
                .content(jsonBody).contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token));

        String responseJson = response.andReturn().getResponse().getContentAsString();
        response.andExpect(MockMvcResultMatchers.status().isOk());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.status").isNotEmpty());
        assert responseJson.contains(String.valueOf(HttpStatus.OK.value()));
        response.andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.message").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.content").doesNotExist());
    }

    @Test
    void handleDeleteRequest_mismatchedDataHolderIds_throwUnauthorized() throws Exception {
        ResultActions response = mvc.perform(delete("/api/v2/users/{id}", "invalidId")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token));

        String responseJson = response.andReturn().getResponse().getContentAsString();
        response.andExpect(MockMvcResultMatchers.status().isUnauthorized());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.status").isNotEmpty());
        assert responseJson.contains(String.valueOf(HttpStatus.UNAUTHORIZED.value()));
        response.andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.message").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.content").doesNotExist());
    }

    @Test
    void handleDeleteRequest_validUserId_successMsg() throws Exception {
        doReturn(Optional.of(validUserData)).when(repository).findById(validUserId);

        ResultActions response = mvc.perform(delete("/api/v2/users/{id}", validUserId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token));

        String responseJson = response.andReturn().getResponse().getContentAsString();
        response.andExpect(MockMvcResultMatchers.status().isOk());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.status").isNotEmpty());
        assert responseJson.contains(String.valueOf(HttpStatus.OK.value()));
        response.andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.message").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.content").doesNotExist());
    }
}