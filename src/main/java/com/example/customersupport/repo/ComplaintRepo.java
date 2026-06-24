package com.example.customersupport.repo;

import com.example.customersupport.entity.Category;
import com.example.customersupport.entity.Complaint;
import com.example.customersupport.enums.ComplaintStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComplaintRepo extends JpaRepository<Complaint,Long> {

    List<Complaint> findAllByComplaintStatus(ComplaintStatus filter, Pageable pageable);
}
