package com.seoLeir.spring.condition;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class JpaCondition implements Condition {
    AnnotatedTypeMetadata metadata;
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        try {
            this.metadata = metadata;
            context.getClassLoader().loadClass("org.postgresql.Driver");

            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }

    }
}
