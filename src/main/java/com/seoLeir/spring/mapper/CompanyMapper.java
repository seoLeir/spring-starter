package com.seoLeir.spring.mapper;

import com.seoLeir.spring.database.entity.Company;
import com.seoLeir.spring.dto.CompanyReadDto;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CompanyMapper {
    CompanyReadDto companyReadFromCompany(Company company);
    List<CompanyReadDto> companyReadListFromCompanyList(List<Company> companies);
}
