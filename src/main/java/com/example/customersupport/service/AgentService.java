package com.example.customersupport.service;

import com.example.customersupport.dto.response.AgentResponseDto;
import com.example.customersupport.enums.SupportType;
import com.example.customersupport.repo.AgentRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AgentService {

    private AgentRepo agentRepo;


    public List<AgentResponseDto> getAllAgents(int page, int size, String sortBy, boolean ascending, String search, SupportType filter) {


    }
}
