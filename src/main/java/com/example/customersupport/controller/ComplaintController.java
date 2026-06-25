package com.example.customersupport.controller;

import com.example.customersupport.dto.response.ComplaintResponseDto;
import com.example.customersupport.enums.ComplaintStatus;
import com.example.customersupport.service.ComplaintService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/complaints")
public class ComplaintController {

    private final ComplaintService complaintService;

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


}
