package com.seoLeir.spring.database.repository;

import com.seoLeir.spring.database.entity.Role;
import com.seoLeir.spring.database.entity.User;
import com.seoLeir.spring.dto.PersonalInfo;
import com.seoLeir.spring.dto.UserFilter;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class FilterAndPageableUserRepositoryImpl implements FilterAndPageableUserRepository {

    private static final String FIND_BY_COMPANY_AND_ROLE = """
            select
                firstname,
                lastname,
                birth_date 
            from users
            where company_id = ?
                and role = ?
            """;
    private static final String UPDATE_COMPANY_AND_ROLE = """
            update users
            set company_id = ?,
                role = ?
            where id = ?
            """;
    private static final String UPDATE_COMPANY_AND_ROLE_NAMED = """
            update users
            set company_id = :companyId,
                role = :role
            where id = :id
            """;

    private final EntityManager entityManager;
    private final CriteriaBuilder criteriaBuilder;
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public FilterAndPageableUserRepositoryImpl(EntityManager entityManager, JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.entityManager = entityManager;
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.criteriaBuilder = this.entityManager.getCriteriaBuilder();
    }

    @Override
    public List<User> findAllByFilter(UserFilter userFilter) {
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<User> userRoot = criteriaQuery.from(User.class);
        Predicate predicate = getPredicate(userFilter, userRoot);
        criteriaQuery.where(predicate);
        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    private Predicate getPredicate(UserFilter userFilter, Root<?> root){
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
    }

    @Override
    public List<PersonalInfo> findAllByCompanyIdAndRole(Integer companyId, Role role) {
        return jdbcTemplate.query(FIND_BY_COMPANY_AND_ROLE,
                (rs, rowNum) -> new PersonalInfo(
                        rs.getString("firstname"),
                        rs.getString("lastname"),
                        rs.getDate("birth_date").toLocalDate()),
                companyId, role.name());
    }

    @Override
    public void updateCompanyAndRole(List<User> users) {
        List<Object[]> args = users.stream()
                .map(user ->
                        new Object[]{user.getCompany().getId(), user.getRole().name(), user.getId()})
                .toList();
        jdbcTemplate.batchUpdate(UPDATE_COMPANY_AND_ROLE, args);
    }

    @Override
    public void updateCompanyAndRoleNamed(List<User> users) {
        MapSqlParameterSource[] args = users.stream().map(user -> Map.of(
                        "companyId", user.getCompany().getId(),
                        "role", user.getRole().name(),
                        "id", user.getId()))
                .map(MapSqlParameterSource::new)
                .toArray(MapSqlParameterSource[]::new);
        namedParameterJdbcTemplate.batchUpdate(UPDATE_COMPANY_AND_ROLE_NAMED, args);
    }

    private long getEmployeesCount(Predicate predicate){
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.TYPE);
        Root<User> userRoot = countQuery.from(User.class);
        countQuery.select(criteriaBuilder.count(userRoot)).where(predicate);
        return entityManager.createQuery(countQuery).getSingleResult();
    }
}
