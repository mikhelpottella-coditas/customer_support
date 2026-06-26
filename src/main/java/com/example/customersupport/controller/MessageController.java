package com.example.customersupport.controller;

import com.example.customersupport.dto.response.GenericResponse;
import com.example.customersupport.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/chat")
@RequiredArgsConstructor
public class MessageController {

    private final ChatService chatService;

    @PostMapping("/complaint/{complaintId}/send")
    public ResponseEntity<GenericResponse> msgSender(@PathVariable Long complaintId, @RequestParam String msg){
        return ResponseEntity.ok(chatService.msgSender(complaintId,msg));
    }

}
