package com.seoLeir.spring.dto;

import lombok.Value;

@Value
public class MessagesResponseDto<T> {
    T data;
}
