package com.example.customersupport.controller;

import com.example.customersupport.service.UserCurdService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/user/")
@RequiredArgsConstructor
public class UserCrudController {

    private final UserCurdService userCurdService;



}
