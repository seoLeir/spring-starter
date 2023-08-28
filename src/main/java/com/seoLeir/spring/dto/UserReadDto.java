package com.seoLeir.spring.dto;

import com.seoLeir.spring.database.entity.Role;
import lombok.Value;

import java.time.LocalDate;

@Value
public class UserReadDto {
    Long id;
    String username;
    LocalDate birthDate;
    String firstName;
    String lastName;
    String image;
    Role role;
    CompanyReadDto companyId;
}
