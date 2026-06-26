package com.example.customersupport.controller;

import com.example.customersupport.dto.response.CustomerResponseDto;
import com.example.customersupport.dto.response.GenericResponse;
import com.example.customersupport.enums.ComplaintStatus;
import com.example.customersupport.service.ComplaintService;
import com.example.customersupport.service.CustomerService;
import com.example.customersupport.service.GeminiService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.ai.chat.model.Generation;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/agent/")
@RequiredArgsConstructor
@Validated
public class AgentController {


    private final ComplaintService complaintService;
    private final GeminiService geminiService;
    private final CustomerService customerService;

    @PostMapping("/{id}/status")
    public ResponseEntity<GenericResponse> updateStatus(@PathVariable Long id, @RequestParam ComplaintStatus complaintStatus){
        return ResponseEntity.ok(complaintService.updateStatus(id,complaintStatus));
    }

    @GetMapping("/customers/{id}")
    public ResponseEntity<CustomerResponseDto> getCustomerById(@NotNull @PathVariable Long id){
        return ResponseEntity.ok(customerService.getCustomerById(id));
    }

    @PostMapping("/support-agent/text")
    public String chatWithGemini(@NotBlank @RequestBody String text){
        return geminiService.getAnswer(text);
    }

}
