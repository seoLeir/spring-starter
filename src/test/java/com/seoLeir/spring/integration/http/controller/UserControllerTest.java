package com.seoLeir.spring.integration.http.controller;

import com.seoLeir.spring.integration.IntegrationTestBase;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import static com.seoLeir.spring.dto.UserCreateEditDto.Fields.*;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@RequiredArgsConstructor
class UserControllerTest extends IntegrationTestBase {
    private final MockMvc mockMvc;


    @SneakyThrows
    @Test
    void findAll(){
        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("users/users"))
                .andExpect(model().attributeExists("users"))
                .andExpect(model().attribute("users", hasSize(5)));
    }

    @SneakyThrows
    @Test
    void create(){
        mockMvc.perform(post("/api/v1/users")
                        .param(username, "test@gmail.com")
                        .param(firstName, "Test")
                        .param(lastName, "Test")
                        .param(role, "ADMIN")
                        .param(companyId, "1")
                        .param(birthDate, "2020-08-08"))
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrlPattern("/api/v1/users/{\\d+}")
                );
    }
}