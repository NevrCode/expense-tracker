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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nevrcode.expense_tracker.entity.User;
import com.nevrcode.expense_tracker.model.RegisterUserRequest;
import com.nevrcode.expense_tracker.model.TokenResponse;
import com.nevrcode.expense_tracker.model.UpdateUserRequest;
import com.nevrcode.expense_tracker.model.UserResponse;
import com.nevrcode.expense_tracker.model.WebResponse;
import com.nevrcode.expense_tracker.repository.UserRepository;
import com.nevrcode.expense_tracker.security.BCrypt;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void testRegisterSuccess() throws Exception {
        RegisterUserRequest req = new RegisterUserRequest();
        req.setUsername("test");
        req.setPassword("rahasia");
        req.setName("aaa");
        
        mockMvc.perform(
            post("/api/user/register")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(req))
            
        ).andExpectAll(
            status().isOk()
        ).andDo(result -> {
            WebResponse<String> res = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {

            });
            assertEquals("OK", res.getData());
        });
    }
    @Test
    void testRegisterBadRequest() throws Exception {
        RegisterUserRequest req = new RegisterUserRequest();
        req.setUsername("");
        req.setPassword("");
        req.setName("");
        
        mockMvc.perform(
            post("/api/user/register")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(req))
            
        ).andExpectAll(
            status().isBadRequest()
        ).andDo(result -> {
            WebResponse<String> res = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {

            });
            assertNotNull(res.getError());
        });
    }
    @Test
    void testRegisterDupped() throws Exception {
        User user = new User();
        user.setUsername("test");
        user.setPasword(BCrypt.hashpw("rahasia", BCrypt.gensalt()));
        user.setName("ayam");
        userRepository.save(user);

        RegisterUserRequest req = new RegisterUserRequest();
        req.setUsername("test");
        req.setPassword("rahasia");
        req.setName("ayam");
        
        mockMvc.perform(
            post("/api/user/register")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(req))
            
        ).andExpectAll(
            status().isBadRequest()
        ).andDo(result -> {
            WebResponse<String> res = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {

            });
            assertNotNull(res.getError());
        });
    }
    @Test
    void getUserUnAuthorizer() throws Exception{

        mockMvc.perform(
            get("/api/user/current")
            .accept(MediaType.APPLICATION_JSON)
            .header("X-API-TOKEN", "notfound")
        ).andExpectAll(
            status().isUnauthorized()
        )
        .andDo(
             result -> {
                WebResponse<String> res = objectMapper.readValue(result.getResponse().getContentAsString(),new TypeReference<>() {
                    
                });
                assertNotNull(res.getError());
             }
        );
    }

    @Test
    void getUserUnAuthorizerTokenNotSent() throws Exception{

        mockMvc.perform(
            get("/api/user/current")
            .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
            status().isUnauthorized()
        )
        .andDo(
             result -> {
                WebResponse<String> res = objectMapper.readValue(result.getResponse().getContentAsString(),new TypeReference<>() {
                    
                });
                assertNotNull(res.getError());
             }
        );
    }

    @Test
    void getUserSuccess() throws Exception{
        User user = new User();
        user.setUsername("Test");
        user.setName("Test");
        user.setPasword("Test");
        user.setToken("Test");
        user.setTokenExpiredAt(System.currentTimeMillis() + 1000000000L);

        userRepository.save(user);
        mockMvc.perform(
            get("/api/user/current")
            .accept(MediaType.APPLICATION_JSON)
            .header("X-API-TOKEN","Test")
        ).andExpectAll(
            status().isOk()
        )
        .andDo(result -> {
                WebResponse<UserResponse> res = objectMapper.readValue(result.getResponse().getContentAsString(),new TypeReference<>() {
                    
                });
                assertNull(res.getError());
                assertEquals("Test", res.getData().getUsername());
                assertEquals("Test", res.getData().getName());
             }
        );
    }

    @Test
    void getUserTokenExpired() throws Exception{
        User user = new User();
        user.setUsername("Test");
        user.setName("Test");
        user.setPasword("Test");
        user.setToken("Test");
        user.setTokenExpiredAt(System.currentTimeMillis() - 1000000000L);

        userRepository.save(user);

        mockMvc.perform(
            get("/api/user/current")
            .accept(MediaType.APPLICATION_JSON)
            .header("X-API-TOKEN","Test")
        ).andExpectAll(
            status().isUnauthorized()
        )
        .andDo(result -> {
                WebResponse<String> res = objectMapper.readValue(
                    result.getResponse()
                    .getContentAsString(),
                    new TypeReference<>() {
                });
                assertNotNull(res.getError());
            });
    }
    @Test
    void updateUserUnauthorized() throws Exception{
        UpdateUserRequest req = new UpdateUserRequest();

        mockMvc.perform(
            patch("/api/user/patch")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(req))
        ).andExpectAll(
            status().isUnauthorized()
        )
        .andDo(
             result -> {
                WebResponse<String> res = objectMapper.readValue(result.getResponse().getContentAsString(),new TypeReference<>() {
                    
                });
                assertNotNull(res.getError());
             }
        );
    }

    @Test
    void updateUserSuccess() throws Exception{
        UpdateUserRequest req = new UpdateUserRequest();
        User user = new User();
        user.setUsername("Test");
        user.setName("Test");
        user.setPasword(BCrypt.hashpw("Test", BCrypt.gensalt()));
        user.setToken("Test");
        user.setTokenExpiredAt(System.currentTimeMillis() + 1000000000L);

        userRepository.save(user);

        req.setName("test");
        req.setPassword("123123");
        mockMvc.perform(
            patch("/api/user/patch")
            .accept(MediaType.APPLICATION_JSON)
            .header("X-API-TOKEN", "Test")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(req))
        ).andExpectAll(
            status().isOk()
        )
        .andDo(
             result -> {
                WebResponse<UserResponse> res = objectMapper.readValue(result.getResponse().getContentAsString(),new TypeReference<>() {
                    
                });
                assertNull(res.getError());
                assertEquals(res.getData().getName(), req.getName());

                User userdb = userRepository.findById("Test").orElse(null);

                assertNotNull(userdb);
                assertTrue(BCrypt.checkpw("123123", userdb.getPasword()));
             }
        );
    }
}
