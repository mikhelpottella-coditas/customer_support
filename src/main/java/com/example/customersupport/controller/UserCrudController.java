package com.example.customersupport.controller;

import com.example.customersupport.dto.response.ProfileResponseDto;
import com.example.customersupport.service.UserCurdService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/user/")
@RequiredArgsConstructor
public class UserCrudController {

    private final UserCurdService userCurdService;

    @GetMapping
    public ResponseEntity<ProfileResponseDto> getProfile(){
        return ResponseEntity.ok(userCurdService.getProfile());
    }

}
