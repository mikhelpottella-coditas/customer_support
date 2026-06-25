package com.example.customersupport.service;

import com.example.customersupport.dto.request.UserRequestDto;
import com.example.customersupport.dto.response.GenericResponse;
import com.example.customersupport.entity.Manager;
import com.example.customersupport.entity.User;
import com.example.customersupport.enums.Roles;
import com.example.customersupport.repo.ManagerRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ManagerService {


    private final ManagerRepo managerRepo;


    public GenericResponse registerManger(UserRequestDto userRequestDto) {
        log.info("seeding the manager into application");
        User user = User.builder()
                .firstName(userRequestDto.firstName())
                .lastName(userRequestDto.lastName())
                .email(userRequestDto.email())
                .phoneNumber(userRequestDto.phone())
                .password(userRequestDto.password())
                .isDeleted(false)
                .createdAt(LocalDateTime.now())
                .role(Roles.MANAGER)
                .build();

        Manager manager = Manager.builder()
                .user(user)
                .build();

        managerRepo.save(manager);

        log.info("new manager is seeded in to the application");
        return new GenericResponse(HttpStatus.CREATED,"new manager is seeded successfully");


    }
}
