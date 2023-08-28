package com.seoLeir.spring.database.repository;

import com.seoLeir.spring.database.entity.User;
import com.seoLeir.spring.dto.UserFilter;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class UserSpecifications {
    public static Specification<User> userFilterSpecification(UserFilter userFilter){
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (userFilter.firstname() != null){
                predicates.add(criteriaBuilder.like(root.get("firstName"), "%" + userFilter.firstname() + "%"));
            }
            if (userFilter.lastname() != null){
                predicates.add(criteriaBuilder.like(root.get("lastName"), "%" + userFilter.lastname() + "%"));
            }
            if (userFilter.birtDate() != null){
                predicates.add(criteriaBuilder.lessThan(root.get("birthDate"), "%" + userFilter.birtDate() + "%"));
            }
            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        };
    }
}
