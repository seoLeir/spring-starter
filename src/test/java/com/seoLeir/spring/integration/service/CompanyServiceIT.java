package com.seoLeir.spring.integration.service;

import com.seoLeir.spring.integration.IntegrationTestBase;
import com.seoLeir.spring.integration.annotation.IT;
import com.seoLeir.spring.config.DatabaseProperties;
import com.seoLeir.spring.dto.CompanyReadDto;
import com.seoLeir.spring.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

//@ExtendWith(SpringExtension.class)
//@ContextConfiguration(classes = ApplicationRunner.class, initializers = ConfigDataApplicationContextInitializer.class)
//@ActiveProfiles("test")
//@SpringBootTest

@RequiredArgsConstructor
public class CompanyServiceIT extends IntegrationTestBase {

    private static final Integer COMPANY_ID = 1;

    private final CompanyService companyService;

    private final DatabaseProperties properties;

    @Test
    void finById(){
        Optional<CompanyReadDto> actualResult = companyService.findById(COMPANY_ID);
        assertTrue(actualResult.isPresent());
        CompanyReadDto expectedResult = new CompanyReadDto(COMPANY_ID, "Google");
        actualResult.ifPresent(actual -> assertEquals(expectedResult, actual));
    }
}
