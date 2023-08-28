package com.seoLeir.spring.service;

import com.seoLeir.spring.database.repository.CompanyRepository;
import com.seoLeir.spring.dto.CompanyReadDto;
import com.seoLeir.spring.listener.entity.AccessType;
import com.seoLeir.spring.listener.entity.EntityEvent;
import com.seoLeir.spring.mapper.CompanyMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class CompanyService {
    private final CompanyRepository companyCrudRepository;
    private final CompanyMapper companyMapper;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public Optional<CompanyReadDto> findById(Integer id){
        return companyCrudRepository.findById(id)
                .map(entity -> {
                    eventPublisher.publishEvent(new EntityEvent(entity, AccessType.READ));
                    return companyMapper.companyReadFromCompany(entity);
                });
    }

    @Transactional(readOnly = true)
    public List<CompanyReadDto> findAll(){
        return companyCrudRepository.findAll().stream()
                .map(companyMapper::companyReadFromCompany)
                .toList();
    }
}
