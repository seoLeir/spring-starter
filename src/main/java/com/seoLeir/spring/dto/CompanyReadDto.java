package com.seoLeir.spring.dto;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import lombok.Value;

@Value
public class CompanyReadDto {
    Integer id;
    String name;
}
