package com.example.customersupport.controller;

import com.example.customersupport.dto.request.InviteRequestDto;
import com.example.customersupport.dto.response.AgentResponseDto;
import com.example.customersupport.dto.response.ComplaintResponseDto;
import com.example.customersupport.dto.response.GenericResponse;
import com.example.customersupport.enums.ComplaintStatus;
import com.example.customersupport.enums.Priority;
import com.example.customersupport.enums.SupportType;
import com.example.customersupport.service.AgentService;
import com.example.customersupport.service.ComplaintService;
import com.example.customersupport.service.InviteService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/manager/")
@Validated
public class ManagerController {

    private final ComplaintService complaintService;
    private final AgentService agentService;
    private final InviteService inviteService;


    @GetMapping("/agents")
    public ResponseEntity<List<AgentResponseDto>> getAllAgents(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "5") int size,
            @RequestParam(required = false, defaultValue = "id") String sortBy,
            @RequestParam(required = false, defaultValue = "true") boolean ascending,
            @RequestParam(required = false, defaultValue = "") String search,
            @RequestParam(required = false) SupportType filter
    ) {
        return ResponseEntity.ok(agentService.getAllAgents(page, size, sortBy, ascending, search, filter));
    }

    @PatchMapping("/complaints/{id}")
    public ResponseEntity<GenericResponse> setPriority(@NotNull @PathVariable Long id,@NotNull @RequestParam Priority priority){
        return ResponseEntity.ok(complaintService.setPriority(id,priority));
    }

    @GetMapping("/agents/{id}")
    public ResponseEntity<AgentResponseDto> getAgentById(@PathVariable Long id) {
        return ResponseEntity.ok(agentService.getAgentById(id));
    }


    @PatchMapping("/agent/assgin")
    public ResponseEntity<GenericResponse> assignComplaint(
            @NotNull @RequestParam Long agentId,
            @NotNull @RequestParam Long complaintId
    ) {
        return ResponseEntity.ok(agentService.assignComplaint(agentId,complaintId));
    }

    @PatchMapping("/agent/reassgin")
    public ResponseEntity<GenericResponse> reassignComplaint(
            @NotNull @RequestParam Long agentId,
            @NotNull @RequestParam Long complaintId
    ) {
        return ResponseEntity.ok(agentService.reassignComplaint(agentId,complaintId));
    }


    @PostMapping("/invite-agent")
    public ResponseEntity<GenericResponse> sendInvite(@Valid @RequestBody InviteRequestDto inviteRequestDto){
        return ResponseEntity.ok(inviteService.invite(inviteRequestDto));
    }


}
