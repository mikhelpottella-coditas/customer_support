package com.example.customersupport.controller;

import com.example.customersupport.dto.request.AttachmentRequestDto;
import com.example.customersupport.dto.request.ComplaintRequestDto;
import com.example.customersupport.dto.request.RatingRequestDto;
import com.example.customersupport.dto.response.ComplaintResponseDto;
import com.example.customersupport.dto.response.GenericResponse;
import com.example.customersupport.service.ComplaintService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/customer")
public class CustomerController {


    private final ComplaintService complaintService;

    // post complaint

    @PostMapping("/complaints")
    public ResponseEntity<GenericResponse> registerComplaint(@Valid @RequestBody ComplaintRequestDto complaintRequestDto){
        return  ResponseEntity.status(HttpStatus.CREATED).body(complaintService.registerComplaint(complaintRequestDto));
    }

    // get all the complaints


    @PostMapping("/{id}/attachment")
    public ResponseEntity<GenericResponse> addAttachment(@PathVariable Long id,@Valid @RequestBody AttachmentRequestDto attachmentRequestDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(complaintService.addAttachment(id,attachmentRequestDto));
    }
    // get the complaint by id


    @PostMapping("/{id}/ratings")
    public ResponseEntity<GenericResponse> giveRating(@PathVariable Long id, @Valid @RequestBody RatingRequestDto ratingRequestDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(complaintService.giveRating(id,ratingRequestDto));
    }

    // post attachment to the complaint

    // post message to the complaint

    // see the messages in the complaint

    // post the rating

}
