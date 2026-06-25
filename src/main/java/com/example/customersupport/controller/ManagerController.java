package com.example.customersupport.controller;

import com.example.customersupport.dto.response.AgentResponseDto;
import com.example.customersupport.dto.response.ComplaintResponseDto;
import com.example.customersupport.enums.ComplaintStatus;
import com.example.customersupport.enums.SupportType;
import com.example.customersupport.service.AgentService;
import com.example.customersupport.service.ComplaintService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/manager/")
public class ManagerController {

    private final ComplaintService complaintService;
    private final AgentService agentService;

    @GetMapping("/complaints")
    public ResponseEntity<List<ComplaintResponseDto>> getAllComplaints(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "5") int size,
            @RequestParam(required = false, defaultValue = "id") String sortBy,
            @RequestParam(required = false, defaultValue = "true") boolean ascending,
            @RequestParam(required = false, defaultValue = "") String search,
            @RequestParam(required = false) ComplaintStatus filter
    ){
        return ResponseEntity.ok(complaintService.getAllComplaints(page,size,sortBy,ascending,search,filter));
    }


    @GetMapping("/complaints/{id}")
    public ResponseEntity<ComplaintResponseDto> getComplaintById(@PathVariable Long id){
        return ResponseEntity.ok(complaintService.getComplaintById(id));
    }


    @GetMapping("/agents")
    public ResponseEntity<List<AgentResponseDto>> getAllAgents(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "5") int size,
            @RequestParam(required = false, defaultValue = "id") String sortBy,
            @RequestParam(required = false, defaultValue = "true") boolean ascending,
            @RequestParam(required = false, defaultValue = "") String search,
            @RequestParam(required = false) SupportType filter
    ){
        return ResponseEntity.ok(agentService.getAllAgents(page,size,sortBy,ascending,search,filter));
    }

}
