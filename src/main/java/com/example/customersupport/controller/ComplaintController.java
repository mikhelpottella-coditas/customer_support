package com.example.customersupport.controller;

import com.example.customersupport.dto.response.ComplaintResponseDto;
import com.example.customersupport.dto.response.GenericResponse;
import com.example.customersupport.enums.ComplaintStatus;
import com.example.customersupport.service.ComplaintService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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




    @GetMapping("/attachment/{attachmentId}")
    public ResponseEntity<byte[]> getAttachment(@NotNull @PathVariable Long attachmentId){
        return complaintService.getAttachmentById(attachmentId);
    }




}
