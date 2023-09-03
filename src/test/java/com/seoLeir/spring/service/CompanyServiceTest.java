package com.seoLeir.spring.service;

import com.seoLeir.spring.database.entity.Company;
import com.seoLeir.spring.database.repository.CompanyRepository;
import com.seoLeir.spring.dto.CompanyReadDto;
import com.seoLeir.spring.listener.entity.EntityEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class, SpringExtension.class})
class CompanyServiceTest {
    private static final Integer COMPANY_ID = 1;

    @Mock
    private UserService userService;

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private CompanyService companyService;

    @Test
    void findById() {
        Company company = Company.builder()
                .id(COMPANY_ID)
                .name("Google")
                .build();
        doReturn(Optional.of(company))
                .when(companyRepository).findById(COMPANY_ID);

        Optional<CompanyReadDto> actualResult = companyService.findById(COMPANY_ID);

        assertFalse(actualResult.isPresent());

        CompanyReadDto expectedResult = new CompanyReadDto(COMPANY_ID, "Google");
        actualResult.ifPresent(actual -> assertEquals(expectedResult, actual));
        verify(eventPublisher).publishEvent(any(EntityEvent.class));

        verifyNoMoreInteractions(eventPublisher, userService);

    }
}