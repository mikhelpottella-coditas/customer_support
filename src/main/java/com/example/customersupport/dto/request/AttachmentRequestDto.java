package com.example.customersupport.dto.request;


import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;

public record AttachmentRequestDto(

        @NotBlank(message = "please provide the reference text")
        String referenceText
) {
}
