package com.example.customersupport.controller;

import com.example.customersupport.dto.request.ComplaintRequestDto;
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



    // get the complaint by id



    // post attachment to the complaint

    // post message to the complaint

    // see the messages in the complaint

    // post the rating

}
