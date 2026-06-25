package com.example.customersupport.service;

import com.example.customersupport.dto.request.AgentRequestDto;
import com.example.customersupport.dto.response.AgentResponseDto;
import com.example.customersupport.dto.response.GenericResponse;
import com.example.customersupport.entity.Agent;
import com.example.customersupport.entity.Invite;
import com.example.customersupport.entity.User;
import com.example.customersupport.enums.InviteStatus;
import com.example.customersupport.enums.Roles;
import com.example.customersupport.enums.SupportType;
import com.example.customersupport.exception.CustomException;
import com.example.customersupport.repo.AgentRepo;
import com.example.customersupport.util.AuthorityUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AgentService {

    private final AuthorityUtil authorityUtil;
    private final AgentRepo agentRepo;
    private final InviteService inviteService;


    public List<AgentResponseDto> getAllAgents(int page, int size, String sortBy, boolean ascending, String search, SupportType filter) {
        // checking the authority of the user


        Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        List<Agent> agentList = agentRepo.findAll(pageable).getContent();


        return null;

    }

    public GenericResponse register(String token, @Valid AgentRequestDto agentRequestDto) {

        Invite invite = inviteService.validate(agentRequestDto.email(),token);



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
            inviteService.updateStatus(invite,InviteStatus.APPROVED);
        } catch (Exception e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "there is problem in saving the data");
        }

      ;

        log.info("registering the agent  in to the application is successful");
        return new GenericResponse(HttpStatus.CREATED, "registering the agent  in to the application is successful");
    }
}
