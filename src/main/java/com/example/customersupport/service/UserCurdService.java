package com.example.customersupport.service;

import com.example.customersupport.dto.request.UserRequestDto;
import com.example.customersupport.dto.response.GenericResponse;
import com.example.customersupport.dto.response.ProfileResponseDto;
import com.example.customersupport.entity.User;
import com.example.customersupport.exception.CustomException;
import com.example.customersupport.repo.UserRepo;
import com.example.customersupport.util.AuthorityUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserCurdService {
    private final AuthorityUtil authorityUtil;
    private final UserRepo userRepo;

    public ProfileResponseDto getProfile() {
        User user = authorityUtil.checkUser();
        if(user==null) throw new CustomException(HttpStatus.UNAUTHORIZED,"please login to access the application");

        ProfileResponseDto profileResponseDto = ProfileResponseDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phone(user.getPhoneNumber())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .build();
        log.info("fetching the profile of the user with id : {}",user.getId());
        return profileResponseDto;
    }


    public GenericResponse updateProfile(UserRequestDto userRequestDto) {
        User user = authorityUtil.checkUser();
        if (userRequestDto.firstName() != null) user.setFirstName(userRequestDto.firstName());
        if (userRequestDto.lastName() != null) user.setLastName(userRequestDto.lastName());
        if (userRequestDto.phone() != null &&
                userRequestDto.phone().length() == 10 &&
                userRequestDto.phone().matches("^\\d+$")) user.setPhoneNumber(userRequestDto.phone());
        try{
            userRepo.save(user);
        } catch (Exception e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR,"something went wrong");
        }
        log.info("the user is updated successfully with the id : {}",user.getId());
        return new GenericResponse(HttpStatus.OK,"profile update successful");
    }
}
