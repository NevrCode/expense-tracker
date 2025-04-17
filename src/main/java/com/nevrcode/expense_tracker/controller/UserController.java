package com.nevrcode.expense_tracker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.nevrcode.expense_tracker.entity.User;
import com.nevrcode.expense_tracker.model.RegisterUserRequest;
import com.nevrcode.expense_tracker.model.UpdateUserRequest;
import com.nevrcode.expense_tracker.model.UserResponse;
import com.nevrcode.expense_tracker.model.WebResponse;
import com.nevrcode.expense_tracker.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;



@RestController
public class UserController {
    
    @Autowired
    private UserService userService;


    @PostMapping(
        path = "/api/user/register", 
        consumes = MediaType.APPLICATION_JSON_VALUE, 
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> register(@RequestBody RegisterUserRequest request) {
        userService.register(request);
        return WebResponse.<String>builder().data("OK").build();
    }
    @GetMapping(
        path = "/api/user/current",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<UserResponse> get(User user) {
        UserResponse userResponse = userService.get(user);

        return WebResponse.<UserResponse>builder().data(userResponse).build();
    }
    @PatchMapping(
        path = "/api/user/patch",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<UserResponse> update(User user,@RequestBody UpdateUserRequest request) {
    
        UserResponse userResponse = userService.update(user, request);
        return WebResponse.<UserResponse>builder().data(userResponse).build();
    }

}
