package com.example.customersupport.dto.request;

import com.example.customersupport.enums.SupportType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.NonNull;

public record InviteRequestDto(
        @NonNull
        @Email(message = "email is not in proper format")
        String sentTo,

        @NotNull(message = "please provide the support type")
        SupportType supportType,

        @NotBlank
        String message
) {
}
