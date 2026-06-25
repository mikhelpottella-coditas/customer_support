package com.example.customersupport.service;

import com.example.customersupport.dto.response.ProfileResponseDto;
import com.example.customersupport.entity.User;
import com.example.customersupport.util.AuthorityUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class UserCurdService {
    private final AuthorityUtil authorityUtil;

    public ProfileResponseDto getProfile() {

        User user = authorityUtil.checkUser();

        ProfileResponseDto profileResponseDto = ProfileResponseDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phone(user.getPhoneNumber())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .build();
        return profileResponseDto;
    }

    //

}
