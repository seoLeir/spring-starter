package com.seoLeir.spring.mapper;

import com.seoLeir.spring.database.entity.Company;
import com.seoLeir.spring.database.entity.User;
import com.seoLeir.spring.database.repository.CompanyRepository;
import com.seoLeir.spring.dto.CompanyReadDto;
import com.seoLeir.spring.dto.UserCreateEditDto;
import com.seoLeir.spring.dto.UserReadDto;
import org.mapstruct.*;
import org.springframework.core.codec.EncodingException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.function.Predicate;


@Mapper(componentModel = "spring",
        uses = CompanyRepository.class,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public abstract class UserMapper {


    @Mappings({
            @Mapping(target = "id", source = "user.id"),
            @Mapping(target = "birthDate", source = "user.birthDate"),
            @Mapping(target = "firstName", source = "user.firstName"),
            @Mapping(target = "lastName", source = "user.lastName"),
            @Mapping(target = "companyId", source = "companyReadDto")
    })
    public abstract UserReadDto userReadFromUser(User user, CompanyReadDto companyReadDto);


    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "company", source = "company"),
            @Mapping(target = "usersChats", ignore = true),
            @Mapping(target = "image", expression = "java(setImage(userCreateEditDto))"),
            @Mapping(target = "password", expression = "java(passwordEncode(userCreateEditDto.getPassword(), encoder))")
    })
    public abstract User userFromUserCreateEdit(UserCreateEditDto userCreateEditDto, Company company, PasswordEncoder encoder);

    @Mappings({
            @Mapping(target = "company", source = "company"),
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "image", expression = "java(setImageToUser(userCreateEditDto, user))"),
            @Mapping(target = "password", expression = "java(passwordEncode(userCreateEditDto.getPassword(), passwordEncoder))")
    })
    public abstract void updateUserFromUserCreateEditDto(UserCreateEditDto userCreateEditDto, @MappingTarget User user, Company company, PasswordEncoder passwordEncoder);

    protected String setImageToUser(UserCreateEditDto editDto, User user){
        Optional.ofNullable(editDto.getImage())
                .filter(Predicate.not(MultipartFile::isEmpty))
                .ifPresent(multipartFile -> user.setImage(multipartFile.getOriginalFilename()));
        return user.getImage();
    }

    protected String passwordEncode(String rawPassword, PasswordEncoder passwordEncoder){
        return Optional.ofNullable(rawPassword)
                .filter(StringUtils::hasText)
                .map(passwordEncoder::encode)
                .orElseThrow(() -> new EncodingException("Could not encode password: " + rawPassword));
    }

    protected String setImage(UserCreateEditDto userCreateEditDto){
        return Optional.ofNullable(userCreateEditDto.getImage())
                .filter(Predicate.not(MultipartFile::isEmpty))
                .map(MultipartFile::getOriginalFilename)
                .orElse(null);
    }
}
