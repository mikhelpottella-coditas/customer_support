package com.example.customersupport.dto.response;

import org.springframework.http.HttpStatus;

public record GenericResponse(
        HttpStatus status,
        String message
) {
}
