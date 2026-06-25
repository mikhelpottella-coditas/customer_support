package com.example.customersupport.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDto (

        @NotBlank(message = "email is not provided")
        @Email(message = "please provide a proper email")
        String email
//
//        @
//        String password
//
){
}
