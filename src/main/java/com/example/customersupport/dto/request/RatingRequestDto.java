package com.example.customersupport.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RatingRequestDto(

        @NotNull(message = "please provide the rating")
        Short rating

) {
}
