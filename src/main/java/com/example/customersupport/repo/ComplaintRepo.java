package com.example.customersupport.repo;

import com.example.customersupport.entity.Category;
import com.example.customersupport.entity.Complaint;
import com.example.customersupport.entity.User;
import com.example.customersupport.enums.ComplaintStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.net.ContentHandler;
import java.util.List;

@Repository
public interface ComplaintRepo extends JpaRepository<Complaint,Long> {

    List<Complaint> findAllByComplaintStatus(ComplaintStatus filter, Pageable pageable);

    Page<Complaint> findAllByAgent_User(User agentUser, Pageable pageable);

    Page<Complaint> findAllByComplaintStatusAndAgent_User(ComplaintStatus complaintStatus, User agentUser,Pageable pageable);

    Page<Complaint> findAllByCustomer_User(User customerUser, Pageable pageable);

    Page<Complaint> findAllByComplaintStatusAndCustomer_User(ComplaintStatus complaintStatus, User customerUser, Pageable pageable);
}
