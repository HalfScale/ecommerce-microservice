package io.muffin.controller;

import io.muffin.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-integrationtest.yml")
public class AuthServiceControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserRepository userRepository;

    @Test
    void testRegisterEndpoint_isOk() throws Exception {
        String jsonContent = "{\"firstName\":\"Jane\",\"lastName\":\"Doe\",\"email\":\"janedoe@gmail.com\",\"password\":\"password\"}";
        mvc.perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(content().json("2")) //expected is 2 since, there is an insert in users in data.sql
                .andExpect(status().isOk());
    }

    @Test
    void testRegisterEndpoint_BadRequest() throws Exception {
        String jsonContent = "{\"lastName\":\"Doe\",\"email\":\"janedoe@gmail.com\",\"password\":\"password\"}";
        mvc.perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testLogin_isOk() throws Exception {
        // please note that, data is already injected through the 'data.sql' under resources folder
        String loginJson = "{\"email\":\"user@email.com\",\"password\":\"password\"}";
        mvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON)
                .content(loginJson))
                .andExpect(status().isOk());
    }

    @Test
    void testLogin_Invalid_Creds() throws Exception {
        // please note that, data is already injected through the 'data.sql' under resources folder
        String loginJson = "{\"email\":\"user@email.com\",\"password\":\"password1\"}";
        mvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON)
                .content(loginJson))
                .andExpect(status().isUnauthorized());
    }

}
