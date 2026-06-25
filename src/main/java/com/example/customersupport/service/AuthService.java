package com.example.customersupport.service;

import com.example.customersupport.dto.request.AgentRequestDto;
import com.example.customersupport.dto.response.GenericResponse;
import com.example.customersupport.entity.Agent;
import com.example.customersupport.entity.Invite;
import com.example.customersupport.entity.User;
import com.example.customersupport.enums.InviteStatus;
import com.example.customersupport.enums.Roles;
import com.example.customersupport.exception.CustomException;
import com.example.customersupport.repo.AgentRepo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final AgentRepo agentRepo;
    private final InviteService inviteService;

    public GenericResponse register(String token, @Valid AgentRequestDto agentRequestDto) {

        Invite invite = inviteService.validate(agentRequestDto.email(), token);
        log.info("registering the agent into application");
        User user = User.builder()
                .firstName(agentRequestDto.firstName())
                .lastName(agentRequestDto.lastName())
                .email(agentRequestDto.email())
                .phoneNumber(agentRequestDto.phone())
                .password(agentRequestDto.password())
                .isDeleted(false)
                .createdAt(LocalDateTime.now())
                .role(Roles.AGENT)
                .build();

        Agent agent = Agent.builder()
                .user(user)
                .supportType(invite.getSupportType())
                .experience(agentRequestDto.experience())
                .build();

        try {
            agentRepo.save(agent);
            inviteService.updateStatus(invite, InviteStatus.APPROVED);
        } catch (Exception e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "there is problem in saving the data");
        }
        log.info("registering the agent  in to the application is successful");
        return new GenericResponse(HttpStatus.CREATED, "registering the agent  in to the application is successful");
    }

}
