package com.example.customersupport.controller;

import com.example.customersupport.dto.response.ComplaintResponseDto;
import com.example.customersupport.service.ComplaintService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/manager/")
public class ManagerController {

    private final ComplaintService complaintService;

    public ResponseEntity<List<ComplaintResponseDto>> getAllComplaints(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "5") int size,
            @RequestParam(required = false, defaultValue = "id") String sortBy,
            @RequestParam(required = false, defaultValue = "true") boolean ascending,
            @RequestParam(required = false, defaultValue = "") String search,
            @RequestParam(required = false,defaultValue = "") String filter
    ){
        return ResponseEntity.ok(complaintService.getAllComplaints(page,size,sortBy,ascending,search,filter));
    }

}
