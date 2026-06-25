package com.example.customersupport.service;

import com.example.customersupport.dto.response.AgentResponseDto;
import com.example.customersupport.entity.Agent;
import com.example.customersupport.enums.SupportType;
import com.example.customersupport.repo.AgentRepo;
import com.example.customersupport.util.AuthorityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AgentService {

    private final AuthorityUtil authorityUtil;
    private final AgentRepo agentRepo;


    public List<AgentResponseDto> getAllAgents(int page, int size, String sortBy, boolean ascending, String search, SupportType filter) {
        // checking the authority of the user



        Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        List<Agent> agentList = agentRepo.findAll(pageable).getContent();




    }
}
