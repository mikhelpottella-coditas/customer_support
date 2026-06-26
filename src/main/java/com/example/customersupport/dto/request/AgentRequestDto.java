package com.example.customersupport.dto.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.NumberFormat;

public record AgentRequestDto (

        @NotBlank(message = "please provide the first name")
        String firstName,

        String lastName,

        @NotBlank(message = "please provide the email")
        @Email(message = "please provide the correct email")
        String email,

        @NotBlank(message = "please provide the password")
        @Size(min = 6,max = 200,message = "please provide the password in the given range")
        String password,

        @NotBlank(message = "please provide the phone number")
        @NumberFormat(style = NumberFormat.Style.NUMBER)
        @Size(min = 10,max = 10)
        String phone


){
}
