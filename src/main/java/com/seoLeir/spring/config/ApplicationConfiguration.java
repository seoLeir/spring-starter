package com.seoLeir.spring.config;

import com.seoLeir.spring.database.pool.ConnectionPool;
import com.seoLeir.spring.database.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.stereotype.Component;

//@Import(WebConfiguration.class)
@Configuration(proxyBeanMethods = true)
@ComponentScan(basePackages = "com.seoLeir.spring",
        useDefaultFilters = false,
        includeFilters = {
                @Filter(type = FilterType.ANNOTATION, value = Component.class),
                @Filter(type = FilterType.ASSIGNABLE_TYPE, value = CompanyRepository.class),
                @Filter(type = FilterType.REGEX, pattern = "com\\..+Repository")
        })
@EnableConfigurationProperties(DatabaseProperties.class)
public class ApplicationConfiguration {
        @Bean(name = "pool2")
        @Scope(BeanDefinition.SCOPE_SINGLETON)
        public ConnectionPool pool2(@Value("${db.username}") String username, @Value("${db.pool.size}") Integer poolSize) {
                return new ConnectionPool(username, 20);
        }

        @Bean
        public ConnectionPool pool3(){
                return new ConnectionPool("test-pool", 30);
        }
}
