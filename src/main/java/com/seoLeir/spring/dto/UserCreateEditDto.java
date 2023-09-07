package com.seoLeir.spring.dto;

import com.seoLeir.spring.database.entity.Role;
import com.seoLeir.spring.validation.group.CreateAction;
import com.seoLeir.spring.validation.UserInfo;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Value;
import lombok.experimental.FieldNameConstants;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Value
@FieldNameConstants
@UserInfo(groups = CreateAction.class)
public class UserCreateEditDto {

    @Email
    String username;

    @NotBlank(groups = CreateAction.class, message = "password may not be blank")
    String password;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate birthDate;

    @Size(min = 3, max = 64)
    String firstName;

    String lastName;

    Role role;

    Integer companyId;

    MultipartFile image;
}
