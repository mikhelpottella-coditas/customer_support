package com.example.customersupport.repo;


import com.example.customersupport.entity.Chatting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChattingRepo extends JpaRepository<Chatting,Long> {
}
