package com.nevrcode.expense_tracker.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.nevrcode.expense_tracker.entity.User;
import com.nevrcode.expense_tracker.model.LoginUserRequest;
import com.nevrcode.expense_tracker.model.TokenResponse;
import com.nevrcode.expense_tracker.repository.UserRepository;
import com.nevrcode.expense_tracker.security.BCrypt;

import jakarta.transaction.Transactional;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValidationService validationService;

    @Transactional
    public TokenResponse login(LoginUserRequest request) {
        validationService.validate(request);

        User user = userRepository.findById(request.getUsername())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username or password are wrong"));

        if(BCrypt.checkpw(request.getPassword(), user.getPasword())) {
            user.setToken(UUID.randomUUID().toString());
            user.setTokenExpiredAt(System.currentTimeMillis() + 1000 * 60 * 60);

            userRepository.save(user);
            return TokenResponse.builder().token(user.getToken()).expiredAt(user.getTokenExpiredAt()).build();
        }
        else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "username or password are wrong");
        }
    }

    @Transactional
    public void logout(User user) {
        user.setToken(null);
        user.setTokenExpiredAt(null);
        
        userRepository.save(user);
    }
}
