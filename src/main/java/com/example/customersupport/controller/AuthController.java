package com.example.customersupport.controller;

import com.example.customersupport.dto.request.UserRequestDto;
import com.example.customersupport.dto.response.GenericResponse;
import com.example.customersupport.service.ManagerService;
import com.example.customersupport.service.UserCurdService;
import com.example.customersupport.service.UserService;
import jakarta.validation.Valid;
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

    @PostMapping("/seeding")
    public ResponseEntity<GenericResponse> registerUser(@Valid @RequestBody UserRequestDto userRequestDto) {
        GenericResponse genericResponse = managerService.registerManger(userRequestDto);
        return new ResponseEntity<>(genericResponse, HttpStatus.CREATED);
    }


    @PostMapping("/register/{invitation}")
    public ResponseEntity<GenericResponse> invitationRegistration(@Valid @RequestBody)


}
