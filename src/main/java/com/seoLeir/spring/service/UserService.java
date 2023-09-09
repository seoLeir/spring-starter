package com.seoLeir.spring.service;

import com.seoLeir.spring.database.entity.Company;
import com.seoLeir.spring.database.entity.User;
import com.seoLeir.spring.database.repository.CompanyRepository;
import com.seoLeir.spring.database.repository.UserRepository;
import com.seoLeir.spring.database.repository.UserSpecifications;
import com.seoLeir.spring.dto.CompanyReadDto;
import com.seoLeir.spring.dto.UserCreateEditDto;
import com.seoLeir.spring.dto.UserFilter;
import com.seoLeir.spring.dto.UserReadDto;
import com.seoLeir.spring.mapper.CompanyMapper;
import com.seoLeir.spring.mapper.CompanyMapperImpl;
import com.seoLeir.spring.mapper.UserMapper;
import com.seoLeir.spring.mapper.UserMapperImpl;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;
    private final ImageService imageService;
    private final UserMapper userMapper = new UserMapperImpl();
    private final CompanyMapper companyMapper = new CompanyMapperImpl();

//    @PostFilter("filterObject.role.name().equals('ADMIN')")
//    @PostFilter("@companyService.findById(filterObject.companyId).isPresent()")
    @Transactional(readOnly = true)
    public List<UserReadDto> findAll(){
        return userRepository.findAll()
                .stream()
                .map(user -> {
            CompanyReadDto companyReadDto = companyMapper.companyReadFromCompany(user.getCompany());
            return userMapper.userReadFromUser(user, companyReadDto);
        }).toList();
    }

    @Transactional(readOnly = true)
    public List<UserReadDto> findAll(UserFilter userFilter){
        Specification<User> userSpecification = UserSpecifications.userFilterSpecification(userFilter);
        return userRepository.findAll(userSpecification).stream()
                .map(user -> {
                    CompanyReadDto companyReadDto = companyMapper.companyReadFromCompany(user.getCompany());
                    return userMapper.userReadFromUser(user, companyReadDto);
                }).toList();
    }

    @Transactional(readOnly = true)
    public Page<UserReadDto> findAll(UserFilter userFilter, Pageable pageable){
        Specification<User> userSpecification = UserSpecifications.userFilterSpecification(userFilter);
        return userRepository.findAll(userSpecification, pageable)
                .map(user -> {
                    CompanyReadDto companyReadDto = companyMapper.companyReadFromCompany(user.getCompany());
                    return userMapper.userReadFromUser(user, companyReadDto);
                });
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
//    @PostAuthorize("returnObject")
    @Transactional(readOnly = true)
    public Optional<UserReadDto> findById(Long id){
        return userRepository.findById(id).
                map(user -> {
                    CompanyReadDto companyReadDto = companyMapper.companyReadFromCompany(user.getCompany());
                    return userMapper.userReadFromUser(user, companyReadDto);
                });
    }

    @Transactional
    public UserReadDto create(UserCreateEditDto userCreateEditDto){
           return Optional.of(userCreateEditDto)
                   .map(dto -> {
                       uploadImage(dto.getImage());
                       Company company = companyRepository.findById(dto.getCompanyId()).orElse(null);
                       return userMapper.userFromUserCreateEdit(dto, company, passwordEncoder);
                   })
                   .map(userRepository::save)
                   .map(user -> {
                       CompanyReadDto companyReadDto = companyMapper.companyReadFromCompany(user.getCompany());
                       return userMapper.userReadFromUser(user, companyReadDto);
                   })
                   .orElseThrow();
    }

    @Transactional
    public Optional<UserReadDto> update(Long id, @Validated UserCreateEditDto dto){
        return userRepository.findById(id)
                .map(user -> {
                    uploadImage(dto.getImage());
                    Company company = companyRepository.findById(dto.getCompanyId()).orElse(null);
                    userMapper.updateUserFromUserCreateEditDto(dto, user, company, passwordEncoder);
                    return user;
                })
                .map(user -> {
                    userRepository.saveAndFlush(user);
                    return user;
                })
                .map(user -> {
                    CompanyReadDto companyReadDto = companyMapper.companyReadFromCompany(user.getCompany());
                    return userMapper.userReadFromUser(user, companyReadDto);
                });
    }

    @SneakyThrows
    private void uploadImage(MultipartFile image) {
        if (!image.isEmpty()){
            imageService.upload(image.getOriginalFilename(), image.getInputStream());
        }
    }

    @Transactional(readOnly = true)
    public Optional<byte[]> findAvatar(Long id){
        return userRepository.findById(id)
                .map(User::getImage)
                .filter(StringUtils::hasText)
                .flatMap(imageService::get);
    }

    @Transactional
    public boolean delete(Long id){
        return userRepository.findById(id)
                .map(entity -> {
                    userRepository.delete(entity);
                    userRepository.flush();
                    return true;
                })
                .orElse(false);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(user -> new org.springframework.security.core.userdetails.User(
                        user.getUsername(),
                        user.getPassword(),
                        Collections.singletonList(user.getRole())
                ))
                .orElseThrow(() -> new UsernameNotFoundException("Filed to retrieve user: " + username));
    }
}
