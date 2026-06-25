package com.example.customersupport.dto.response;


import com.example.customersupport.enums.SupportType;
import lombok.Builder;

@Builder
public record AgentResponseDto(

        Long id,

        String firstName,

        String lastName,

        String email,

        String phone,

        Double experience,

        Double rating,

        SupportType supportType


) {
}
