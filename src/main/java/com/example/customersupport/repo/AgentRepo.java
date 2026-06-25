package com.example.customersupport.repo;

import com.example.customersupport.entity.Agent;
import com.example.customersupport.enums.SupportType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgentRepo extends JpaRepository<Agent,Long> {
    Page<Agent> findAllBySupportType(SupportType supportType, Pageable pageable);
}
