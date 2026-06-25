package com.example.customersupport.controller;

import com.example.customersupport.dto.request.AttachmentRequestDto;
import com.example.customersupport.dto.response.ComplaintResponseDto;
import com.example.customersupport.dto.response.GenericResponse;
import com.example.customersupport.enums.ComplaintStatus;
import com.example.customersupport.service.ComplaintService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/complaints")
public class ComplaintController {

    private final ComplaintService complaintService;


    @GetMapping()
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


    @GetMapping("/{id}")
    public ResponseEntity<ComplaintResponseDto> getComplaintById(@PathVariable Long id){
        return ResponseEntity.ok(complaintService.getComplaintById(id));
    }

    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @PostMapping("/{id}/attachment")
    public ResponseEntity<GenericResponse> addAttachment(@PathVariable Long id,@Valid @RequestBody AttachmentRequestDto attachmentRequestDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(complaintService.addAttachment(id,attachmentRequestDto));
    }

    @PostMapping("/{id}/messages")
    public ResponseEntity<GenericResponse> sendMessage(@PathVariable Long id,@RequestParam String message){
        return ResponseEntity.status(HttpStatus.CREATED).body(complaintService.sendMessage(id,message));
    }
}
