package io.rently.userservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.rently.userservice.configs.TestConfigs;
import io.rently.userservice.dtos.User;
import io.rently.userservice.errors.enums.Errors;
import io.rently.userservice.services.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(UserController.class)
@ContextConfiguration(classes = TestConfigs.class)
class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserService userService;

    @Test
    void getRequest_invalidUri_isMethodNotAllowed() throws Exception {
        String invalidUri = "/invalid/uri/path";

        ResultActions response = mvc.perform(
                MockMvcRequestBuilders
                .get(invalidUri));

        String responseJson = response.andReturn().getResponse().getContentAsString();
        response.andExpect(MockMvcResultMatchers.status().isMethodNotAllowed());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.status").isNotEmpty());
        assert responseJson.contains(String.valueOf(HttpStatus.METHOD_NOT_ALLOWED.value()));
        response.andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.message").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.content").doesNotExist());
    }

    @Test
    void getRequest_validUri_invalidUserId_notFound() throws Exception {
        String validUri = "/api/v1/users/{id}";
        String invalidUserId = "invalidUserId";

        Mockito.when(userService.returnUserById(invalidUserId)).thenThrow(Errors.USER_NOT_FOUND.getException());

        ResultActions response = mvc.perform(
                MockMvcRequestBuilders
                .get(validUri, invalidUserId));

        String responseJson = response.andReturn().getResponse().getContentAsString();
        response.andExpect(MockMvcResultMatchers.status().isNotFound());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.status").isNotEmpty());
        assert responseJson.contains(String.valueOf(HttpStatus.NOT_FOUND.value()));
        response.andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.message").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.content").doesNotExist());
    }

    @Test
    void getRequest_validUri_validUserId_isOk() throws Exception {
        String validUri = "/api/v1/users/{id}";
        String validUserId = "validUserId";
        User user = new User.Builder().setId("validUserId").build();

        Mockito.when(userService.returnUserById("validUserId")).thenReturn(user);

        ResultActions response = mvc.perform(
                MockMvcRequestBuilders
                .get(validUri, validUserId));

        String responseJson = response.andReturn().getResponse().getContentAsString();
        response.andExpect(MockMvcResultMatchers.status().isOk());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.status").isNotEmpty());
        assert responseJson.contains(String.valueOf(HttpStatus.OK.value()));
        response.andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.message").doesNotExist());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.content").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.content.id").isNotEmpty());
        assert responseJson.contains(validUserId);
    }

    @Test
    void postRequest_invalidUri_isMethodNotAllowed() throws Exception {
        String invalidUri = "/invalid/uri/path";

        ResultActions response = mvc.perform(
                MockMvcRequestBuilders
                .post(invalidUri));

        String responseJson = response.andReturn().getResponse().getContentAsString();
        response.andExpect(MockMvcResultMatchers.status().isMethodNotAllowed());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.status").isNotEmpty());
        assert responseJson.contains(String.valueOf(HttpStatus.METHOD_NOT_ALLOWED.value()));
        response.andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.message").isNotEmpty());
    }

    @Test
    void postRequest_validUri_noRequestBody_notAcceptable() throws Exception {
        String validUri = "/api/v1/users/";

        ResultActions response = mvc.perform(
                MockMvcRequestBuilders
                .post(validUri)
                .contentType(MediaType.APPLICATION_JSON));

        String responseJson = response.andReturn().getResponse().getContentAsString();
        response.andExpect(MockMvcResultMatchers.status().isNotAcceptable());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.status").isNotEmpty());
        assert responseJson.contains(String.valueOf(HttpStatus.NOT_ACCEPTABLE.value()));
        response.andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.message").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.content").doesNotExist());
    }

    @Test
    void postRequest_validUri_invalidRequestBody_notAcceptable() throws Exception {
        String validUri = "/api/v1/users/";
        String invalidRequestBody = "invalidRequestBody";

        ResultActions response = mvc.perform(
                MockMvcRequestBuilders
                .post(validUri)
                .content(invalidRequestBody)
                .contentType(MediaType.APPLICATION_JSON));

        String responseJson = response.andReturn().getResponse().getContentAsString();
        response.andExpect(MockMvcResultMatchers.status().isNotAcceptable());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.status").isNotEmpty());
        assert responseJson.contains(String.valueOf(HttpStatus.NOT_ACCEPTABLE.value()));
        response.andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.message").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.content").doesNotExist());
    }

    @Test
    void postRequest_validUri_validRequestBody_isOk() throws Exception {
        String validUri = "/api/v1/users/";
        User validUserData = new User.Builder()
                .setUsername("validPassword")
                .setEmail("validEmail")
                .setPassword("validPassword").build();
        String jsonBody = new ObjectMapper().writeValueAsString(validUserData);

        ResultActions response = mvc.perform(
                MockMvcRequestBuilders
                .post(validUri)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON));

        String responseJson = response.andReturn().getResponse().getContentAsString();
        response.andExpect(MockMvcResultMatchers.status().isOk());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.status").isNotEmpty());
        assert responseJson.contains(String.valueOf(HttpStatus.OK.value()));
        response.andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.message").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.content").doesNotExist());
    }

    @Test
    void postRequest_validUri_existingUsernameRequestBody_isConflict() throws Exception {
        String validUri = "/api/v1/users/";
        String existingUsername = "existingUsername";
        User existingUserData = new User
                .Builder()
                .setUsername(existingUsername)
                .build();
        String jsonBody = new ObjectMapper().writeValueAsString(existingUserData);

        Mockito.doThrow(Errors.USERNAME_ALREADY_EXISTS.getException())
                .when(userService)
                .addUser(Mockito.any(existingUserData.getClass()));

        ResultActions response = mvc.perform(
                MockMvcRequestBuilders
                .post(validUri)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON));

        String responseJson = response.andReturn().getResponse().getContentAsString();
        response.andExpect(MockMvcResultMatchers.status().isConflict());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.status").isNotEmpty());
        assert responseJson.contains(String.valueOf(HttpStatus.CONFLICT.value()));
        response.andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.message").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.content").doesNotExist());
    }

    @Test
    void putRequest_invalidUri_isMethodNotAllowed() throws Exception {
        String invalidUri = "/invalid/uri/path";

        ResultActions response = mvc.perform(
                MockMvcRequestBuilders
                .put(invalidUri));

        String responseJson = response.andReturn().getResponse().getContentAsString();
        response.andExpect(MockMvcResultMatchers.status().isMethodNotAllowed());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.status").isNotEmpty());
        assert responseJson.contains(String.valueOf(HttpStatus.METHOD_NOT_ALLOWED.value()));
        response.andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.message").isNotEmpty());
    }

    @Test
    void putRequest_validUri_noRequestBody_notAcceptable() throws Exception {
        String validUri = "/api/v1/users/{id}";
        String invalidUserId = "invalidUserId";

        ResultActions response = mvc.perform(
                MockMvcRequestBuilders
                .put(validUri, invalidUserId));

        String responseJson = response.andReturn().getResponse().getContentAsString();
        response.andExpect(MockMvcResultMatchers.status().isNotAcceptable());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.status").isNotEmpty());
        assert responseJson.contains(String.valueOf(HttpStatus.NOT_ACCEPTABLE.value()));
        response.andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.message").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.content").doesNotExist());
    }

    @Test
    void putRequest_validUri_invalidRequestBody_notAcceptable() throws Exception {
        String validUri = "/api/v1/users/{id}";
        String validUserId = "validUserId";
        String invalidRequestBody = "invalidRequestBody";

        ResultActions response = mvc.perform(
                MockMvcRequestBuilders
                .put(validUri, validUserId)
                .content(invalidRequestBody)
                .contentType(MediaType.APPLICATION_JSON));

        String responseJson = response.andReturn().getResponse().getContentAsString();
        response.andExpect(MockMvcResultMatchers.status().isNotAcceptable());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.status").isNotEmpty());
        assert responseJson.contains(String.valueOf(HttpStatus.NOT_ACCEPTABLE.value()));
        response.andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.message").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.content").doesNotExist());
    }

    @Test
    void putRequest_validUri_validRequestBody_invalidUserId_notFound() throws Exception {
        String validUri = "/api/v1/users/{id}";
        String validUserId = "validUserId";
        User validUserData = new User.Builder()
                .setId(validUserId)
                .setUsername("validUpdatedPassword")
                .setEmail("validUpdatedEmail")
                .setPassword("validUpdatedPassword").build();
        String jsonBody = new ObjectMapper().writeValueAsString(validUserData);

        Mockito.doThrow(Errors.USER_NOT_FOUND.getException())
                .when(userService)
                .updateUserById(Mockito.any(validUserId.getClass()), Mockito.any(validUserData.getClass()));

        ResultActions response = mvc.perform(
                MockMvcRequestBuilders
                .put(validUri, validUserId)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON));

        String responseJson = response.andReturn().getResponse().getContentAsString();
        response.andExpect(MockMvcResultMatchers.status().isNotFound());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.status").isNotEmpty());
        assert responseJson.contains(String.valueOf(HttpStatus.NOT_FOUND.value()));
        response.andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.message").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.content").doesNotExist());
    }

    @Test
    void putRequest_validUri_validRequestBody_validUserId_existingUsernameEmail_isConflict() throws Exception {
        String validUri = "/api/v1/users/{id}";
        String validUserId = "validUserId";
        User validUserData = new User.Builder()
                .setId(validUserId)
                .setUsername("validUpdatedPassword")
                .setEmail("validUpdatedEmail")
                .setPassword("validUpdatedPassword").build();
        String jsonBody = new ObjectMapper().writeValueAsString(validUserData);

        Mockito.doThrow(Errors.USERNAME_ALREADY_EXISTS.getException())
                .when(userService)
                .updateUserById(Mockito.any(validUserId.getClass()), Mockito.any(validUserData.getClass()));

        ResultActions response = mvc.perform(
                MockMvcRequestBuilders
                .put(validUri, validUserId)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON));

        String responseJson = response.andReturn().getResponse().getContentAsString();
        response.andExpect(MockMvcResultMatchers.status().isConflict());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.status").isNotEmpty());
        assert responseJson.contains(String.valueOf(HttpStatus.CONFLICT.value()));
        response.andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.message").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.content").doesNotExist());
    }

    @Test
    void putRequest_validUri_validRequestBody_validUserId_isOk() throws Exception {
        String validUri = "/api/v1/users/{id}";
        String validUserId = "validUserId";
        User validUserData = new User.Builder()
                .setId(validUserId)
                .setUsername("validUpdatedPassword")
                .setEmail("validUpdatedEmail")
                .setPassword("validUpdatedPassword").build();
        String jsonBody = new ObjectMapper().writeValueAsString(validUserData);


        Mockito.doNothing()
                .when(userService)
                .updateUserById(Mockito.any(validUserId.getClass()), Mockito.any(validUserData.getClass()));

        ResultActions response1 = mvc.perform(
                MockMvcRequestBuilders
                        .put(validUri, validUserId)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON));

        String responseJson = response1.andReturn().getResponse().getContentAsString();
        response1.andExpect(MockMvcResultMatchers.status().isOk());
        response1.andExpect(MockMvcResultMatchers.jsonPath("$.status").isNotEmpty());
        assert responseJson.contains(String.valueOf(HttpStatus.OK.value()));
        response1.andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").isNotEmpty());
        response1.andExpect(MockMvcResultMatchers.jsonPath("$.message").isNotEmpty());
        response1.andExpect(MockMvcResultMatchers.jsonPath("$.content").doesNotExist());
    }

    @Test
    void deleteRequest_invalidUri_isMethodNotAllowed() throws Exception {
        String invalidUri = "/invalid/uri/path";

        ResultActions response = mvc.perform(
                MockMvcRequestBuilders
                .delete(invalidUri));

        String responseJson = response.andReturn().getResponse().getContentAsString();
        response.andExpect(MockMvcResultMatchers.status().isMethodNotAllowed());
        assert responseJson.contains(String.valueOf(HttpStatus.METHOD_NOT_ALLOWED.value()));
        response.andExpect(MockMvcResultMatchers.jsonPath("$.status").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.message").isNotEmpty());
    }

    @Test
    void deleteRequest_validUri_invalidUserId_notFound() throws Exception {
        String validUri = "/api/v1/users/someUserId";
        String invalidUserId = "invalidUserId";

        Mockito.doThrow(Errors.USER_NOT_FOUND.getException())
                .when(userService)
                .deleteUserById(Mockito.any(invalidUserId.getClass()));

        ResultActions response = mvc.perform(MockMvcRequestBuilders
                .delete(validUri)
                .accept(MediaType.APPLICATION_JSON));

        String responseJson = response.andReturn().getResponse().getContentAsString();
        response.andExpect(MockMvcResultMatchers.status().isNotFound());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.status").isNotEmpty());
        assert responseJson.contains(String.valueOf(HttpStatus.NOT_FOUND.value()));
        response.andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.message").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.content").doesNotExist());
    }

    @Test
    void deleteRequest_validUri_validUserId_isOk() throws Exception {
        String validUri = "/api/v1/users/someUserId";
        String invalidUserId = "invalidUserId";


        Mockito.doNothing()
                .when(userService)
                .deleteUserById(Mockito.any(invalidUserId.getClass()));

        ResultActions response = mvc.perform(MockMvcRequestBuilders
                .delete(validUri)
                .accept(MediaType.APPLICATION_JSON));

        String responseJson = response.andReturn().getResponse().getContentAsString();
        response.andExpect(MockMvcResultMatchers.status().isOk());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.status").isNotEmpty());
        assert responseJson.contains(String.valueOf(HttpStatus.OK.value()));
        response.andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.message").isNotEmpty());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.content").doesNotExist());
    }
}