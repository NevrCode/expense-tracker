package com.nevrcode.expense_tracker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.nevrcode.expense_tracker.entity.User;
import com.nevrcode.expense_tracker.model.LoginUserRequest;
import com.nevrcode.expense_tracker.model.TokenResponse;
import com.nevrcode.expense_tracker.model.WebResponse;
import com.nevrcode.expense_tracker.service.AuthService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    @PostMapping(
        path = "/api/auth/login",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<TokenResponse> login(@RequestBody LoginUserRequest req) {
        TokenResponse tokenResponse = authService.login(req);
        return WebResponse.<TokenResponse>builder().data(tokenResponse).build();
    }

    @DeleteMapping(
        path = "/api/auth/logout",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> logout(User user) {
        authService.logout(user);

        return WebResponse.<String>builder().data("OK").build();
    }
}
