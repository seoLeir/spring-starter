package com.seoLeir.spring;

import com.seoLeir.spring.database.entity.Role;
import com.seoLeir.spring.dto.UserCreateEditDto;
import com.seoLeir.spring.mapper.UserMapper;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.ConfigurableApplicationContext;

import java.time.LocalDate;


@ConfigurationPropertiesScan
@SpringBootApplication
@OpenAPIDefinition
public class ApplicationRunner {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(ApplicationRunner.class, args);
        context.getBeanDefinitionCount();
    }
}