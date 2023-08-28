package com.seoLeir.spring.integration.annotation;

import com.seoLeir.spring.ApplicationRunner;
import com.seoLeir.spring.TestApplicationRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ActiveProfiles("test")
@SpringBootTest(classes = ApplicationRunner.class)
@Transactional
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
public @interface IT {
}
