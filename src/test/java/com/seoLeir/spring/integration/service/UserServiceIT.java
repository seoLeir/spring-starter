package com.seoLeir.spring.integration.service;

import com.seoLeir.spring.database.entity.Role;
import com.seoLeir.spring.database.repository.CompanyRepository;
import com.seoLeir.spring.dto.UserCreateEditDto;
import com.seoLeir.spring.dto.UserReadDto;
import com.seoLeir.spring.integration.IntegrationTestBase;
import com.seoLeir.spring.service.UserService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@RequiredArgsConstructor
public class UserServiceIT extends IntegrationTestBase {
    private final UserService userService;

    private static final Long USER_1 = 1L;
    private static final Integer COMPANY_ID = 1;

    @Test
    void findAll(){
        List<UserReadDto> actualResultList = userService.findAll();
        assertThat(actualResultList).hasSize(5);
    }

    @Test
    void findById(){
        Optional<UserReadDto> user = userService.findById(USER_1);
        assertThat(user).isPresent();
        user.ifPresent(userReadDto -> assertThat(userReadDto.getUsername()).isEqualTo("ivan@gmail.com"));
    }

    @Test
    void create(){
        UserCreateEditDto userCreateEditDto = new UserCreateEditDto(
                "test@gmail.com",
                "123",
                LocalDate.now(),
                "test-firstname",
                "test-lastname",
                Role.ADMIN,
                COMPANY_ID,
                null
        );
        UserReadDto actualResult = userService.create(userCreateEditDto);
        assertAll(
                () -> assertThat(actualResult.getUsername()).isEqualTo(userCreateEditDto.getUsername()),
                () -> assertThat(actualResult.getBirthDate()).isEqualTo(userCreateEditDto.getBirthDate()),
                () -> assertThat(actualResult.getFirstName()).isEqualTo(userCreateEditDto.getFirstName()),
                () -> assertThat(actualResult.getLastName()).isEqualTo(userCreateEditDto.getLastName()),
                () -> assertThat(actualResult.getRole()).isSameAs(userCreateEditDto.getRole()),
                () -> assertThat(actualResult.getCompanyId().getId()).isEqualTo(userCreateEditDto.getCompanyId())
        );
    }

    @Test
    void update(){
        UserCreateEditDto userCreateEditDto = new UserCreateEditDto(
                "test@gmail.com",
                "123",
                LocalDate.now(),
                "test-firstname",
                "test-lastname",
                Role.ADMIN,
                COMPANY_ID,
                null
        );
        Optional<UserReadDto> actualResult = userService.update(USER_1, userCreateEditDto);
        assertThat(actualResult).isPresent();
        actualResult.ifPresent(user -> {
            assertAll(
                    () -> assertThat(user.getUsername()).isEqualTo(userCreateEditDto.getUsername()),
                    () -> assertThat(user.getBirthDate()).isEqualTo(userCreateEditDto.getBirthDate()),
                    () -> assertThat(user.getFirstName()).isEqualTo(userCreateEditDto.getFirstName()),
                    () -> assertThat(user.getLastName()).isEqualTo(userCreateEditDto.getLastName()),
                    () -> assertThat(user.getRole()).isSameAs(userCreateEditDto.getRole()),
                    () -> assertThat(user.getCompanyId().getId()).isEqualTo(userCreateEditDto.getCompanyId())
            );
        });
    }

    @Test
    void delete(){
        boolean result = userService.delete(USER_1);
        assertThat(result).isTrue();
    }

}
