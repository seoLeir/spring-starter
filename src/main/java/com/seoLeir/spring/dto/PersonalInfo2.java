package com.seoLeir.spring.dto;

import org.springframework.beans.factory.annotation.Value;

public interface PersonalInfo2 {
    String getFirstName();
    String getLastName();
    String getBirthDate();

    @Value("#{target.firstName + ' ' + target.lastName}")
    String getFullName();
}
