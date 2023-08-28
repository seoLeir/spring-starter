package com.seoLeir.spring.integration.database.repository;

import com.seoLeir.spring.database.entity.Role;
import com.seoLeir.spring.database.entity.User;
import com.seoLeir.spring.database.repository.UserRepository;
import com.seoLeir.spring.dto.PersonalInfo;
import com.seoLeir.spring.dto.UserFilter;
import com.seoLeir.spring.integration.IntegrationTestBase;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@RequiredArgsConstructor
class UserRepositoryTest extends IntegrationTestBase {

    private final UserRepository userRepository;

    @Test
    void checkButch(){
        List<User> users = userRepository.findAll();
        userRepository.updateCompanyAndRole(users);
    }

    @Test
    void checkJdbcTemplate(){
        List<PersonalInfo> users = userRepository.findAllByCompanyIdAndRole(1, Role.USER);
        assertThat(users).hasSize(1);
    }

    @Test
    void checkAuditing(){
        User user = userRepository.findById(1L).get();
        user.setBirthDate(user.getBirthDate().plusYears(1L));
        userRepository.flush();
        System.out.println();
    }



    @Test
    void checkCustomImplementation(){
        UserFilter filter = new UserFilter(
                null, "ov", LocalDate.now()
        );
        userRepository.findAllByFilter(filter);
    }


    @Test
    void checkProjection(){
        var users = userRepository.findAllByCompanyId(1);
        assertThat(users).hasSize(2);
    }


    @Test
    void checkPageable(){
        PageRequest pageRequest = PageRequest.of(0, 2, Sort.by("id"));
        Page<User> slice = userRepository.findAllBy(pageRequest);
        System.out.println("Total elements: " + slice.getTotalElements());
        System.out.println("Total pages: " + slice.getTotalPages());

        slice.forEach(user -> System.out.println(user.getCompany().getLocales()));
        while (slice.hasNext()){
            slice = userRepository.findAllBy(slice.nextPageable());
            slice.forEach(user -> System.out.println(user.getCompany().getLocales()));
        }

    }

    @Test
    void checkSort(){
        Sort.TypedSort<User> sortBy = Sort.sort(User.class);
        Sort sort = sortBy.by(User::getFirstName).and(sortBy.by(User::getLastName));

        List<User> allUsers = userRepository.findTop3ByBirthDateBefore(LocalDate.now(), sort);
        assertThat(allUsers).hasSize(3);
    }

    @Test
    void checkFirstTop(){

        Optional<User> topUser = userRepository.findTopByOrderByIdDesc();
        assertThat(topUser).isPresent();
        topUser.ifPresent(user -> assertThat(user.getId()).isEqualTo(5L));
    }

    @Test
    void checkUpdate(){
        User ivan = userRepository.getReferenceById(1L);
        assertThat(ivan.getRole()).isEqualTo(Role.ADMIN);
        ivan.setBirthDate(LocalDate.now());

        int resultCount = userRepository.updateRoles(Role.USER, 1L, 5L);
        assertThat(resultCount).isEqualTo(2);

        User theSameIvan = userRepository.getReferenceById(1L);
        assertThat(theSameIvan.getRole()).isEqualTo(Role.USER);
    }

    @Test
    void checkQueries(){
        List<User> users = userRepository.findAllBy("a", "ov");
        assertThat(users).hasSize(3);
        System.out.println(users);
    }
}