package com.example.customersupport.controller;

import com.example.customersupport.dto.request.UserSeedingRequestDto;
import com.example.customersupport.dto.response.GenericResponse;
import com.example.customersupport.service.ManagerService;
import com.example.customersupport.service.UserCurdService;
import com.example.customersupport.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth/")
@RequiredArgsConstructor
public class AuthController {

    private final UserCurdService userCurdService;
    private final ManagerService managerService;

    @PostMapping("/register")
    public ResponseEntity<GenericResponse> registerUser(@RequestBody UserSeedingRequestDto userSeedingRequestDto) {
        GenericResponse genericResponse = managerService.registerManger(userSeedingRequestDto);
        return new ResponseEntity<>(genericResponse, HttpStatus.CREATED);
    }

}
