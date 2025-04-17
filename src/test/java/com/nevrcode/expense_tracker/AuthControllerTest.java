package com.nevrcode.expense_tracker;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import javax.print.attribute.standard.Media;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nevrcode.expense_tracker.entity.User;
import com.nevrcode.expense_tracker.model.LoginUserRequest;
import com.nevrcode.expense_tracker.model.RegisterUserRequest;
import com.nevrcode.expense_tracker.model.TokenResponse;
import com.nevrcode.expense_tracker.model.WebResponse;
import com.nevrcode.expense_tracker.repository.UserRepository;
import com.nevrcode.expense_tracker.security.BCrypt;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ObjectMapper objectMapper;
    
    @Test
    void loginFailedUserNotFound() throws Exception {

        LoginUserRequest req = new LoginUserRequest();
        req.setUsername("wiwi");
        req.setPassword("rahasia");
        
        System.out.println(objectMapper.writeValueAsString(req));
        mockMvc.perform(
            post("/api/auth/login")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(req))
        ).andExpect(
            status().isUnauthorized()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                
            });
            assertNotNull(response.getError());
        });

    }
    @Test
    void loginFailedWrongPassword() throws Exception {
        User user = new User();
        user.setUsername("wiwi");
        user.setPasword(BCrypt.hashpw("salah", BCrypt.gensalt()));
        user.setName("dd");
        userRepository.save(user);
        LoginUserRequest req = new LoginUserRequest();
        req.setUsername("wiwi");
        req.setPassword("rahasia");
        
        mockMvc.perform(
            post("/api/auth/login")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(req))
        ).andExpect(
            status().isUnauthorized()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                
            });
            assertNotNull(response.getError());
        });

    }
    @Test
    void loginSuccess() throws Exception {
        User user = new User();
        user.setUsername("wiwi");
        user.setPasword(BCrypt.hashpw("rahasia", BCrypt.gensalt()));
        user.setName("dd");
        userRepository.save(user);
        LoginUserRequest req = new LoginUserRequest();
        req.setUsername("wiwi");
        req.setPassword("rahasia");
        
        mockMvc.perform(
            post("/api/auth/login")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(req))
        ).andExpect(
            status().isOk()
        ).andDo(result -> {
            WebResponse<TokenResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                
            });
            assertNull(response.getError());
            assertNotNull(response.getData().getToken());
            assertNotNull(response.getData().getExpiredAt());

            User userDB = userRepository.findById("wiwi").orElse(null);
            assertNotNull(userDB);
            assertEquals(userDB.getToken(), response.getData().getToken());
            assertEquals(userDB.getTokenExpiredAt(), response.getData().getExpiredAt());
        });

    }
    @Test
    void logoutFailed() throws Exception{
        mockMvc.perform(
            delete("/api/auth/logout")
            .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
            status().isUnauthorized()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                
            });
            assertNotNull(response.getError());

        });
    }

    @Test
    void logoutSuccess() throws Exception {
        User user = new User();
        user.setUsername("wiwi");
        user.setPasword(BCrypt.hashpw("rahasia", BCrypt.gensalt()));
        user.setName("dd");
        user.setToken("Test");
        user.setTokenExpiredAt(System.currentTimeMillis() + 1000000000L);
        userRepository.save(user);
        
        mockMvc.perform(
            delete("/api/auth/logout")
            .accept(MediaType.APPLICATION_JSON)
            .header("X-API-TOKEN", "Test")
        ).andExpect(
            status().isOk()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                
            });
            assertNull(response.getError());
            assertEquals("OK", response.getData());

            User userdb = userRepository.findById("Test").orElse(null);
            assertNotNull(userdb);
            assertNull(userdb.getToken());
            assertNull(userdb.getTokenExpiredAt());
        });
    }
}
