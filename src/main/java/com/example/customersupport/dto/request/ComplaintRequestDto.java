package com.example.customersupport.dto.request;


import com.example.customersupport.entity.Category;
import com.example.customersupport.enums.Priority;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.util.List;

public record ComplaintRequestDto(

        Long category,

        String description,

        List<AttachmentRequestDto> attachmentRequestDtoList

        ) {
}
