package com.seoLeir.spring.integration.database.repository;

import com.seoLeir.spring.database.entity.Company;
import com.seoLeir.spring.database.repository.CompanyRepository;
import com.seoLeir.spring.integration.IntegrationTestBase;
import com.seoLeir.spring.integration.annotation.IT;
import com.seoLeir.spring.service.CompanyService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;


@RequiredArgsConstructor
class CompanyRepositoryTest extends IntegrationTestBase {
    
    private static final Integer APPLE_ID = 5;
    private final EntityManager entityManager;

    private final CompanyRepository companyRepository;

    private final TransactionTemplate transactionTemplate;

    @Test
    @Disabled
    void delete(){
//        Optional<Company> maybeCompany = companyRepository.findById(APPLE_ID);
//        assertThat(maybeCompany).isPresent();
//        maybeCompany.ifPresent(companyRepository::delete);
//        entityManager.flush();
//        assertThat(companyRepository.findById(APPLE_ID)).isEmpty();
    }

    @Test
    void checkFindByQueries(){
//        companyRepository.findByName("Google");
//        companyRepository.findAllByNameContainingIgnoreCase("a");
    }

    @Test
    void findById() {
//        transactionTemplate.executeWithoutResult(tx -> {
//            Company company = entityManager.find(Company.class, 1);
//            assertThat(company).isNotNull();
//            assertThat(company.getLocales()).hasSize(2);
//        });
    }

    @Test
    void save(){
//        var company = Company.builder()
//                .name("Apple2")
//                .locales(Map.of(
//                        "ru", "Apple описание",
//                        "en", "Apple description"))
//                .build();
//        entityManager.persist(company);
//        assertThat(company.getId()).isNotNull();
    }

}