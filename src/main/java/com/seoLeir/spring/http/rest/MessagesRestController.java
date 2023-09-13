package com.seoLeir.spring.http.rest;

import com.seoLeir.spring.dto.MessagesRequestDto;
import com.seoLeir.spring.dto.MessagesResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RestController
@RequestMapping("/api/v1/messages")
@RequiredArgsConstructor
public class MessagesRestController {
    private final MessageSource messageSource;


    @GetMapping
    public ResponseEntity<MessagesResponseDto<String>> getMessage(@RequestParam("key") String key, @RequestParam("lang") String language){
        String sourceMessage = messageSource.getMessage(key, null, "default message", new Locale(language));
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new MessagesResponseDto<>(sourceMessage));
    }

}
