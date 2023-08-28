package com.seoLeir.spring.database.repository;

import com.seoLeir.spring.database.entity.Role;
import com.seoLeir.spring.database.entity.User;
import com.seoLeir.spring.dto.PersonalInfo;
import com.seoLeir.spring.dto.UserFilter;
import com.seoLeir.spring.dto.UserReadDto;
import org.mapstruct.control.MappingControl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.function.Predicate;

public interface FilterAndPageableUserRepository {

    List<User> findAllByFilter(UserFilter userFilter);

    List<PersonalInfo> findAllByCompanyIdAndRole(Integer companyId, Role role);

    void updateCompanyAndRole(List<User> users);
    void updateCompanyAndRoleNamed(List<User> users);
}
