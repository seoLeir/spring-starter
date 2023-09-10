package com.seoLeir.spring.config;

import com.seoLeir.spring.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.SecurityFilterChain;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Set;

import static com.seoLeir.spring.database.entity.Role.ADMIN;
import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final UserService userService;

    @SneakyThrows
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http){
        http.authorizeHttpRequests((requests) -> requests
                .requestMatchers("/api/v1/login", "/api/v1/users/registration", "/v3/api-docs/**", "/swagger-ui/**").permitAll()
                .requestMatchers("/api/v1/admin").hasAuthority(ADMIN.getAuthority())
                .requestMatchers("/api/v1/users/*/delete").hasAuthority(ADMIN.getAuthority())
                .anyRequest().authenticated());
        http.formLogin(httpSecurityFormLoginConfigurer ->
                httpSecurityFormLoginConfigurer
                        .loginPage("/api/v1/login")
                        .defaultSuccessUrl("/api/v1/users")
                        .permitAll());
        http.logout(httpSecurityLogoutConfigurer ->
                httpSecurityLogoutConfigurer
                        .logoutUrl("/api/v1/logout")
                        .logoutSuccessUrl("/api/v1/login")
                        .deleteCookies("JSESSIONID"));
        http.httpBasic(withDefaults());
        http.oauth2Login(oauth2Configurer -> oauth2Configurer
                .loginPage("/api/v1/login")
                .defaultSuccessUrl("/api/v1/users")
                .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig.oidcUserService(oidcUserService())));
        return http.build();
    }


    private OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService(){

        return userRequest -> {
            String email = userRequest.getIdToken().getClaim("email");
//          TODO: 11.09.2023 Create new user -> userService.create()
//            new OidcUserService().loadUser(userRequest);
            UserDetails userDetails = userService.loadUserByUsername(email);
            DefaultOidcUser oidcUser = new DefaultOidcUser(userDetails.getAuthorities(), userRequest.getIdToken());
            Set<Method> userDetailsMethods = Set.of(UserDetails.class.getMethods());
            return (OidcUser) Proxy.newProxyInstance(SecurityConfiguration.class.getClassLoader(),
                    new Class[]{UserDetails.class, OidcUser.class},
                    (proxy, method, args) -> userDetailsMethods.contains(method)
                                            ? method.invoke(userDetails, args)
                                            : method.invoke(oidcUser, args));
        };
    }
}
