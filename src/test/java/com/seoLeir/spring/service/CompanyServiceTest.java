package com.seoLeir.spring.service;

import com.seoLeir.spring.database.repository.CompanyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.junit.jupiter.SpringExtension;

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
//        doReturn(Optional.of(new Company(COMPANY_ID)))
//                .when(companyRepository).findById(COMPANY_ID);
//
//        Optional<CompanyReadDto> actualResult = companyService.findById(COMPANY_ID);
//
//        assertFalse(actualResult.isPresent());
//
//        CompanyReadDto expectedResult = new CompanyReadDto(COMPANY_ID);
//        actualResult.ifPresent(actual -> assertEquals(expectedResult, actual));
//        verify(eventPublisher).publishEvent(any(EntityEvent.class));
//
//        verifyNoMoreInteractions(eventPublisher, userService);

    }
}