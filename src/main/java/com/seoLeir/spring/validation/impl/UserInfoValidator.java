package com.seoLeir.spring.validation.impl;

import com.seoLeir.spring.dto.UserCreateEditDto;
import com.seoLeir.spring.validation.UserInfo;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import static org.springframework.util.StringUtils.*;

public class UserInfoValidator implements ConstraintValidator<UserInfo, UserCreateEditDto> {
    @Override
    public boolean isValid(UserCreateEditDto value, ConstraintValidatorContext context) {
        return hasText(value.getFirstName()) || hasText(value.getLastName());
    }
}
