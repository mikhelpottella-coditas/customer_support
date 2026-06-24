package com.example.customersupport.dto.response;


import com.example.customersupport.enums.SupportType;

public record AgentResponseDto(

        Long id,

        String firstName,

        String lastName,

        String email,

        String password,

        String phone,

        Double experience,

        Double rating,

        SupportType supportType


) {
}
