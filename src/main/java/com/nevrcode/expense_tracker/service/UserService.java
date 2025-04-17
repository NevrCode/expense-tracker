package com.nevrcode.expense_tracker.service;


import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.nevrcode.expense_tracker.entity.User;
import com.nevrcode.expense_tracker.model.RegisterUserRequest;
import com.nevrcode.expense_tracker.model.UpdateUserRequest;
import com.nevrcode.expense_tracker.model.UserResponse;
import com.nevrcode.expense_tracker.repository.UserRepository;
import com.nevrcode.expense_tracker.security.BCrypt;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private ValidationService validationService;


    @Transactional
    public void register(RegisterUserRequest request) {
        validationService.validate(request);

        if (userRepository.existsById(request.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Username already Exist!");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPasword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
        user.setName(request.getName());

        userRepository.save(user);
    }

    public UserResponse get(User user) {
        return UserResponse.builder().username(user.getUsername()).name(user.getName()).build();
    }

    @Transactional
    public UserResponse update(User user, UpdateUserRequest request) {
        validationService.validate(request);

        if(Objects.nonNull(request.getName())) {
            user.setName(request.getName());
        }
        if(Objects.nonNull(request.getPassword())) {
            user.setPasword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
        }
        userRepository.save(user);

        return UserResponse.builder().username(user.getUsername()).name(user.getName()).build();
    }




}
