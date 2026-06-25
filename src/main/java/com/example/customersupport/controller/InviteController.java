package com.example.customersupport.controller;

import com.example.customersupport.dto.request.InviteRequestDto;
import com.example.customersupport.dto.response.GenericResponse;
import com.example.customersupport.service.InviteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/invite")
@RequiredArgsConstructor
public class InviteController {

    private final InviteService inviteService;


    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_MANAGER')")
    public ResponseEntity<GenericResponse> sendInvite(@Valid @RequestBody InviteRequestDto inviteRequestDto){
        return ResponseEntity.ok(inviteService.invite(inviteRequestDto));
    }

}
