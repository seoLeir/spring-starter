package com.seoLeir.spring.database.repository;

import com.seoLeir.spring.database.entity.Role;
import com.seoLeir.spring.database.entity.User;
import com.seoLeir.spring.dto.PersonalInfo2;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends
        JpaRepository<User, Long>,
        FilterAndPageableUserRepository,
        RevisionRepository<User, Long, Integer>, JpaSpecificationExecutor<User>{

    @Query("select u from User u " +
            "where u.firstName like %:firstName% and u.lastName like %:lastname%")
    List<User> findAllBy(String firstName, String lastname);


    @Modifying(clearAutomatically = true)
    @Query("update User u set u.role = :role where u.id in (:ids)")
    int updateRoles(Role role, Long... ids);

    Optional<User> findTopByOrderByIdDesc();

    @QueryHints(@QueryHint(name = "org.hibernate.fetchSize", value = "50"))
    @Lock(LockModeType.PESSIMISTIC_READ)
    List<User> findTop3ByBirthDateBefore(LocalDate birthDate, Sort sort);

    @EntityGraph(attributePaths = {"company", "company.locales"})
    @Query(value = "select u from User u",
            countQuery = "select count(distinct u.firstName) from User u")
    Page<User> findAllBy(Pageable pageable);


    @Query(value = "select firstname, lastname, birth_date as birthDate from users where company_id = :companyId",
            nativeQuery = true)
     List<PersonalInfo2> findAllByCompanyId(Integer companyId);

    @Modifying(flushAutomatically = true)
    @Query("update User u set " +
            "u.username = :#{#user.username}, " +
            "u.birthDate = :#{#user.birthDate}, " +
            "u.firstName = :#{#user.firstName}, " +
            "u.lastName = :#{#user.lastName}, " +
            "u.role = :#{#user.role}, " +
            "u.company = :#{#user.company}, " +
            "u.image = :#{user.image} " +
            "where u.id = :#{#user.id}")
    void update(@Param("user") User user);

    Optional<User> findByUsername(String username);
}
