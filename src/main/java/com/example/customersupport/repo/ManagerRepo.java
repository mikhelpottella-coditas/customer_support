package com.example.customersupport.repo;

import com.example.customersupport.entity.Manager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManagerRepo extends JpaRepository<Manager, Long> {
}
