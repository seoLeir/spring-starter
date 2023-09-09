package com.seoLeir.spring.http.controller;

import com.seoLeir.spring.database.entity.Role;
import com.seoLeir.spring.dto.PageResponse;
import com.seoLeir.spring.dto.UserCreateEditDto;
import com.seoLeir.spring.dto.UserFilter;
import com.seoLeir.spring.dto.UserReadDto;
import com.seoLeir.spring.service.CompanyService;
import com.seoLeir.spring.service.UserService;
import com.seoLeir.spring.validation.group.CreateAction;
import com.seoLeir.spring.validation.group.UpdateAction;
import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;
import java.util.Optional;


@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private final CompanyService companyService;

    @GetMapping
    public String findAll(Model model, UserFilter userFilter, Pageable pageable){
        Page<UserReadDto> page = userService.findAll(userFilter, pageable);
        model.addAttribute("users", PageResponse.of(page));
        model.addAttribute("filter", userFilter);
        return "users/users";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable("id") Long id, Model model){
        return userService.findById(id)
                .map(userReadDto -> {
                    model.addAttribute("user", userService.findById(id).orElse(null));
                    model.addAttribute("roles", Role.values());
                    model.addAttribute("companies", companyService.findAll());
                    return "users/user";
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/registration")
    public String registration(Model model, @ModelAttribute("user") UserCreateEditDto user){
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        model.addAttribute("companies", companyService.findAll());
        return "users/registration";
    }

    @PostMapping
//    @ResponseStatus(HttpStatus.CREATED)
    public String createUser(@ModelAttribute("user") @Validated({Default.class, CreateAction.class}) UserCreateEditDto user,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes){
        if (bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("user", user);
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/api/v1/users/registration";
        }
        userService.create(user);
        return "redirect:/api/v1/login";
    }

//    @PutMapping("/{id}")
    @PostMapping("/{id}/update")
    public String update(@PathVariable("id") Long id,
                         @Validated({Default.class, UpdateAction.class}) UserCreateEditDto user){
        Optional<UserReadDto> update = userService.update(id, user);
        if (update.isPresent()){
            return update.map(userReadDto -> "redirect:/api/v1/users/{id}").orElse(null);
        }else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

//    @DeleteMapping("/{id}")
    @PostMapping("/{id}/delete")
    public String deleteById(@PathVariable("id") Long id){
        if (!userService.delete(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return "redirect:/api/v1/users";
    }

}
