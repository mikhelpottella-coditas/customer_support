package com.example.customersupport.controller;

import com.example.customersupport.dto.response.GenericResponse;
import com.example.customersupport.enums.ComplaintStatus;
import com.example.customersupport.service.ComplaintService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/agent/")
@RequiredArgsConstructor
public class AgentController {


    private final ComplaintService complaintService;

    @PostMapping("/{id}/status")
    public ResponseEntity<GenericResponse> updateStatus(@PathVariable Long id, @RequestParam ComplaintStatus complaintStatus){
        return ResponseEntity.ok(complaintService.updateStatus(id,complaintStatus));
    }

}
