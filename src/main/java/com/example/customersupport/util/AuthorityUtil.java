package com.example.customersupport.util;

import com.example.customersupport.entity.User;
import com.example.customersupport.enums.Roles;
import com.example.customersupport.exception.CustomException;
import com.example.customersupport.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthorityUtil {

    private final UserService userService;

    public User checkUser(){
        User user = userService.getByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        log.info("checking if the user is a manager or not");
        return user;
    }

}
