package com.seoLeir.spring.integration.http.controller;

import com.seoLeir.spring.database.entity.Role;
import com.seoLeir.spring.integration.IntegrationTestBase;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.util.*;

import static com.seoLeir.spring.database.entity.Role.ADMIN;
import static com.seoLeir.spring.database.entity.Role.USER;
import static com.seoLeir.spring.dto.UserCreateEditDto.Fields.*;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@RequiredArgsConstructor
class UserControllerTest extends IntegrationTestBase {
    private final MockMvc mockMvc;

    @BeforeEach
    void init(){
//        List<GrantedAuthority> roles = Arrays.asList(ADMIN, USER);
//        User testUser = new User("test@gmail.com", "test", roles);
//        TestingAuthenticationToken authenticationToken = new TestingAuthenticationToken(testUser, testUser.getPassword(), roles);
//        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
//        securityContext.setAuthentication(authenticationToken);
//        SecurityContextHolder.setContext(securityContext);
    }

    @SneakyThrows
    @Test
    void findAll(){
        mockMvc.perform(get("/api/v1/users")
                        .with(SecurityMockMvcRequestPostProcessors.user("test@gmail.com").password("test").authorities(ADMIN)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("users/users"))
                .andExpect(model().attributeExists("users"));
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