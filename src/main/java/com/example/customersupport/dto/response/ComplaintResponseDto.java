package com.example.customersupport.dto.response;


import com.example.customersupport.enums.ComplaintStatus;
import lombok.Builder;

import java.util.List;
@Builder
public record ComplaintResponseDto(

        Long id,

        ComplaintStatus complaintStatus,

        String category,

        String description,

        Long agentId,

        Long CustomerId,

        List<Long> attachmentList

) {
}
