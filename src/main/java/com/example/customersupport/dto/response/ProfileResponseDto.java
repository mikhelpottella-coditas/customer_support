package com.example.customersupport.dto.response;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import org.springframework.format.annotation.NumberFormat;

import java.time.LocalDateTime;

@Builder
public record ProfileResponseDto (

        Long id,

        String firstName,

        String lastName,

        String email,

        String phone,

        LocalDateTime createdAt
){
}
