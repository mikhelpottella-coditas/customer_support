package com.example.customersupport.repo;

import com.example.customersupport.entity.Agent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgentRepo extends JpaRepository<Agent,Long> {
}
