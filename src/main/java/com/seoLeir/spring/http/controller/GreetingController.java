package com.seoLeir.spring.http.controller;

import com.seoLeir.spring.database.entity.Role;
import com.seoLeir.spring.dto.UserReadDto;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/api/v1")
@SessionAttributes("user")
public class GreetingController {

//    @ModelAttribute("roles")
//    public List<Role> roles(){
//        return Arrays.asList(Role.values());
//    }

    @GetMapping("/hello")
    public String hello(Model model, HttpServletRequest request, UserReadDto userReadDto){
//        request.setAttribute("user", new UserReadDto(1L,"Ivan")); requestScope
//        request.getSession().setAttribute(); sessionScope
        model.addAttribute("user", userReadDto);
        return "greeting/hello";
    }
    @GetMapping("/hello2")
    public ModelAndView hello2(ModelAndView modelAndView, HttpServletRequest request,
                              @RequestParam("age") Integer age,
                              @RequestHeader("accept") String accept,
                              @CookieValue("JSESSIONID") String jSessionId){
        String ageParam = request.getParameter("age");
        String acceptHeader = request.getHeader("accept");
        Cookie[] cookies = request.getCookies();
        modelAndView.setViewName("greeting/hello");
        return modelAndView;
    }

    @GetMapping("/bye")
    public String bye(ModelAndView modelAndView, @SessionAttribute("user") UserReadDto user){
//        request.getSession.getAttribute("user")
        modelAndView.setViewName("greeting/bye");
        return "greeting/bye";
    }
}
