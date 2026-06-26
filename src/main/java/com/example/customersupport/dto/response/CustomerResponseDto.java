package com.example.customersupport.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record CustomerResponseDto(
        Long id,

        String firstName,

        String lastName,

        String email,

        String phone,

        LocalDateTime createdAt,

        List<ComplaintResponseDto> complaintResponseDtoList
) {

}
