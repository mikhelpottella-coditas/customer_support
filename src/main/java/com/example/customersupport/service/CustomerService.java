package com.example.customersupport.service;

import com.example.customersupport.dto.response.ComplaintResponseDto;
import com.example.customersupport.dto.response.CustomerResponseDto;
import com.example.customersupport.entity.Complaint;
import com.example.customersupport.entity.Customer;
import com.example.customersupport.entity.User;
import com.example.customersupport.exception.CustomException;
import com.example.customersupport.repo.CustomerRepo;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerService {
    private final CustomerRepo customerRepo;

    public Customer findCustomerByUser(User user) {
        return customerRepo.findCustomerByUser(user).orElseThrow(()-> new CustomException(HttpStatus.NOT_FOUND,"customer is not there with the given id"));
    }

    public CustomerResponseDto getCustomerById(@NotNull Long id) {
        Customer customer = customerRepo.findById(id).orElseThrow(()-> new CustomException(HttpStatus.NOT_FOUND,"customer not found with the given id"));
        List<Complaint> complaintList = customer.getComplaintList();
        User user = customer.getUser();

        List<ComplaintResponseDto> complaintResponseDtoList = new ArrayList<>();

        complaintList.forEach(complaint->{

            List<Long> attachemtList = complaint.getAttachmentList() == null ? null : complaint.getAttachmentList()
                    .stream().map(i -> i.getId()).toList();

            Long agentId = complaint.getAgent() == null ? null : complaint.getAgent().getId();

            complaintResponseDtoList.add(ComplaintResponseDto.builder()
                    .id(complaint.getId())
                    .complaintStatus(complaint.getComplaintStatus())
                    .category(complaint.getCategory().getIssue())
                    .description(complaint.getDescription())
                    .CustomerId(complaint.getCustomer().getId())
                    .agentId(agentId)
                    .attachmentList(attachemtList)
                    .build());
        });

        log.info("fetching the customer with the id : {}",id);
        return CustomerResponseDto.builder()
                .id(customer.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phone(user.getPhoneNumber())
                .createdAt(user.getCreatedAt())
                .complaintResponseDtoList(complaintResponseDtoList)
                .build();

    }
}
