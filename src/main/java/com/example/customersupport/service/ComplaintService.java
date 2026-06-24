package com.example.customersupport.service;

import com.example.customersupport.dto.response.ComplaintResponseDto;
import com.example.customersupport.entity.Complaint;
import com.example.customersupport.repo.ComplaintRepo;
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
public class ComplaintService {

    private final ComplaintRepo complaintRepo;


    public List<ComplaintResponseDto> getAllComplaints(int page, int size, String sortBy, boolean ascending, String search, String filter) {

        Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        List<Complaint> complaintList;
        if(filter.isBlank()) complaintList = complaintRepo.findAll(pageable).getContent();
        else {



            complaintList = complaintRepo.findAllByCategory(filter,pageable)
        }



    }
}
