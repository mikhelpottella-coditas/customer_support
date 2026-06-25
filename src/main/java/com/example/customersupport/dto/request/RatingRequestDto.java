package com.example.customersupport.dto.request;

import jakarta.validation.constraints.Size;

public record RatingRequestDto(

        @Size(min = 0,max = 5,message = "rating can be in between 1 to 5")
        Short rating

) {
}
