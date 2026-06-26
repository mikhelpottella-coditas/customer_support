package com.example.customersupport.service;

import com.example.customersupport.dto.request.AgentRequestDto;
import com.example.customersupport.dto.response.AgentResponseDto;
import com.example.customersupport.dto.response.ComplaintResponseDto;
import com.example.customersupport.dto.response.GenericResponse;
import com.example.customersupport.entity.Agent;
import com.example.customersupport.entity.Complaint;
import com.example.customersupport.entity.User;
import com.example.customersupport.enums.ComplaintStatus;
import com.example.customersupport.enums.SupportType;
import com.example.customersupport.exception.CustomException;
import com.example.customersupport.repo.AgentRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AgentService {

    private final AgentRepo agentRepo;
    private final ComplaintService complaintService;
    private final MailService mailService;
    private final SimpMessageSendingOperations messageTemplate;


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
                        .deleted(user.isDeleted())
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
                .deleted(user.isDeleted())
                .build();
    }

    public GenericResponse assignComplaint(Long agentId, Long complaintId) {
        Agent agent = getById(agentId);
        if(agent.getUser().isDeleted()) throw new CustomException(HttpStatus.BAD_REQUEST,"user is deleted");
        Complaint complaint = complaintService.getById(complaintId);

        if (complaint.getAgent() != null)
            throw new CustomException(HttpStatus.BAD_REQUEST, "the agent is already assigned. if you want please try reassigning");
        try {
            complaint.setAgent(agent);
            complaintService.saveCompliant(complaint);
        } catch (RuntimeException e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "something went wrong!!");
        }
        log.info("the assignment of agent with  id : {} to compliant id: {} is done", agentId, complaintId);

        // send email to the customer when ever the agent is assigned
        mailService.mailSender(complaint.getCustomer().getUser().getEmail(),
                "the agent with the email "+agent.getUser().getEmail()+
                        " is assigned to you complaint on"+LocalDateTime.now()+"" +
                        ".\n thank you being patient, he will connect you shortly.",
                "Assigned an agent to the complaint with ticket no. "+complaint.getId());

        // this is for web socket purpose
        List<ComplaintResponseDto> complaintResponseDtoList = complaintService.getAllComplaints(0,20,"id", true, "", ComplaintStatus.RAISED);
        messageTemplate.convertAndSend("/topic/complaints",complaintResponseDtoList);
        return new GenericResponse(HttpStatus.OK, "assignment successful");
    }

    public GenericResponse reassignComplaint(Long agentId, Long complaintId) {
        Agent agent = getById(agentId);
        if(agent.getUser().isDeleted()) throw new CustomException(HttpStatus.BAD_REQUEST,"user is deleted");
        Complaint complaint = complaintService.getById(complaintId);

        try {
            complaint.setAgent(agent);
            complaintService.saveCompliant(complaint);
        } catch (RuntimeException e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "something went wrong!!");
        }
        log.info("the assignment of agent with  id : {} to compliant id: {} is done", agentId, complaintId);
        // send email to the customer when ever the agent is reassigned
        mailService.mailSender(complaint.getCustomer().getUser().getEmail(),
                "the agent with the email " + agent.getUser().getEmail() +
                        " is reassigned to you complaint on" + LocalDateTime.now() +
                        ".\n thank you being patient, he will connect you shortly. sorry for the inconvenience",
                "Assigned an agent to the complaint with ticket no. " + complaint.getId());
        // this is for web socket purpose
        List<ComplaintResponseDto> complaintResponseDtoList = complaintService.getAllComplaints(0,20,"id", true, "", ComplaintStatus.RAISED);
        messageTemplate.convertAndSend("/topic/complaints",complaintResponseDtoList);
        return new GenericResponse(HttpStatus.OK, "assignment successful");
    }
}
