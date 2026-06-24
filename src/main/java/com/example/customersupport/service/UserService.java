package com.example.customersupport.service;

import com.example.customersupport.entity.User;
import com.example.customersupport.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepo userRepo;

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findUsersByEmail(username);
    }
}
