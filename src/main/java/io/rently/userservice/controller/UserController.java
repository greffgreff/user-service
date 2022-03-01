package io.rently.userservice.controller;

import io.rently.userservice.dto.User;
import io.rently.userservice.dto.ResponseBody;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.UUID;

@RestController
public class UserController implements ErrorController {
    public static final String ROUTE = "api/v1/users";

    @GetMapping(value = ROUTE)
    public static ResponseBody getUser() {
        Object data = new User
                .Builder(UUID.randomUUID().toString())
                .setUsername("branlix2000")
                .setFullname("Noah Greff")
                .setEmail("something@gmail.com")
                .setPhone("+31 06 41 53 14")
                .build();

        ResponseBody response = new ResponseBody
                .Builder(new Timestamp(System.currentTimeMillis()), 200)
                .setData(data)
                .build();

        return response;
    }

    @GetMapping("/error")
    public static ResponseBody error() {
        return new ResponseBody.Builder(new Timestamp(System.currentTimeMillis()), 500)
                .setData("Invalid URL path")
                .build();
    }
}
