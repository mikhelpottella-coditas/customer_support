package com.example.customersupport.service;

import com.example.customersupport.dto.request.AgentRequestDto;
import com.example.customersupport.dto.response.AgentResponseDto;
import com.example.customersupport.dto.response.ComplaintResponseDto;
import com.example.customersupport.dto.response.GenericResponse;
import com.example.customersupport.entity.Agent;
import com.example.customersupport.entity.Complaint;
import com.example.customersupport.entity.Invite;
import com.example.customersupport.entity.User;
import com.example.customersupport.enums.InviteStatus;
import com.example.customersupport.enums.Roles;
import com.example.customersupport.enums.SupportType;
import com.example.customersupport.exception.CustomException;
import com.example.customersupport.repo.AgentRepo;
import com.example.customersupport.repo.ComplaintRepo;
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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AgentService {

    private final AgentRepo agentRepo;
    private final ComplaintService complaintService;


    public List<AgentResponseDto> getAllAgents(int page, int size, String sortBy, boolean ascending, String search, SupportType filter) {

        Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        List<Agent> agentList;
        if (filter == null) agentList = agentRepo.findAll(pageable).getContent();
        else agentList = agentRepo.findAllBySupportType(filter, pageable).getContent();
        List<AgentResponseDto> agentResponseDtoList = new ArrayList<>();
        try {
            agentList.forEach(agent -> {
                User user = agent.getUser();
                agentResponseDtoList.add(AgentResponseDto.builder()
                        .id(agent.getId())
                        .email(user.getEmail())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .rating(agent.getRating())
                        .phone(user.getPhoneNumber())
                        .supportType(agent.getSupportType())
                        .build());
            });
        } catch (Exception e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "something went wrong");
        }
        log.info("if the search is empty then return the whole list");
        if (search.isBlank()) return agentResponseDtoList;

        log.info("if the is not empty the filter out the thing and return");
        return agentResponseDtoList.stream().filter(agent -> (agent.firstName() + agent.lastName()).contains(search)).toList();
    }


    private Agent getById(Long id) {
        return agentRepo.findById(id).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "the Agent not found with the given Id : " + id));
    }

    public AgentResponseDto getAgentById(Long id) {

        Agent agent = getById(id);
        User user = agent.getUser();
        return AgentResponseDto.builder()
                .id(agent.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .rating(agent.getRating())
                .phone(user.getPhoneNumber())
                .supportType(agent.getSupportType())
                .build();
    }

    public GenericResponse assignComplaint(Long agentId, Long complaintId) {
        Agent agent = getById(agentId);
        Complaint complaint = complaintService.getById(complaintId);

        if(complaint.getAgent()!=null) throw new CustomException(HttpStatus.BAD_REQUEST,"the agent is already assigned. if you want please try reassigning");
        try {
            complaint.setAgent(agent);
            complaintService.saveCompliant(complaint);
        } catch (RuntimeException e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR,"something went wrong!!");
        }
        log.info("the assignment of agent with  id : {} to compliant id: {} is done",agentId,complaintId);
        return new GenericResponse(HttpStatus.OK,"assignment successful");
    }

    public GenericResponse reassignComplaint(Long agentId, Long complaintId) {
        Agent agent = getById(agentId);
        Complaint complaint = complaintService.getById(complaintId);

        try {
            complaint.setAgent(agent);
            complaintService.saveCompliant(complaint);
        } catch (RuntimeException e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR,"something went wrong!!");
        }
        log.info("the assignment of agent with  id : {} to compliant id: {} is done",agentId,complaintId);
        return new GenericResponse(HttpStatus.OK,"assignment successful");
    }
}
