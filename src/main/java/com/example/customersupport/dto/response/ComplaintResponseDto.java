package com.example.customersupport.dto.response;


import com.example.customersupport.enums.ComplaintStatus;

import java.util.List;

public record ComplaintResponseDto(

        Long id,

        ComplaintStatus complaintStatus,

        String category,

        String description,

        Long agentId,

        Long CustomerId,

        List<Long> imageId

) {
}
