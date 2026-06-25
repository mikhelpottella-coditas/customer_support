package com.example.customersupport.controller;

import com.example.customersupport.dto.request.AgentRequestDto;
import com.example.customersupport.dto.request.LoginRequestDto;
import com.example.customersupport.dto.request.UserRequestDto;
import com.example.customersupport.dto.response.GenericResponse;
import com.example.customersupport.service.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/v1/auth/")
@RequiredArgsConstructor
@Validated
public class AuthController {

    private final ManagerService managerService;
    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/seeding")
    public ResponseEntity<GenericResponse> registerUser(@Valid @RequestBody UserRequestDto userRequestDto) {
        GenericResponse genericResponse = managerService.registerManger(userRequestDto);
        return new ResponseEntity<>(genericResponse, HttpStatus.CREATED);
    }


    @PostMapping("/register/{invitation}")
    public ResponseEntity<GenericResponse> invitationRegistration(@PathVariable String token, @Valid @RequestBody AgentRequestDto agentRequestDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.tokenRegister(token,agentRequestDto));
    }

    @PostMapping("/register/")
    public ResponseEntity<GenericResponse> Registration(@Valid @RequestBody UserRequestDto userRequestDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(userRequestDto));
    }


    @GetMapping("/refresh/{token}")
    public ResponseEntity<GenericResponse> getAccessToken(@NotBlank @PathVariable String token){
        return ResponseEntity.ok(refreshTokenService.refresh(token));
    }

    @PostMapping("/login")
    public ResponseEntity<GenericResponse> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        return ResponseEntity.ok(authService.login(loginRequestDto));
    }


    @DeleteMapping("/logout")
    public ResponseEntity<GenericResponse> logout(){
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(authService.logout());
    }

    @PatchMapping("/signout")
    public ResponseEntity<GenericResponse> signOut(){
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(authService.signOut());
    }

}
