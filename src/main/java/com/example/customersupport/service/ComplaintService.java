package com.example.customersupport.service;

import com.example.customersupport.dto.response.ComplaintResponseDto;
import com.example.customersupport.entity.Category;
import com.example.customersupport.entity.Complaint;
import com.example.customersupport.entity.User;
import com.example.customersupport.enums.ComplaintStatus;
import com.example.customersupport.enums.Roles;
import com.example.customersupport.exception.CustomException;
import com.example.customersupport.repo.ComplaintRepo;
import com.example.customersupport.util.AuthorityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ComplaintService {

    private final ComplaintRepo complaintRepo;
    private final CategoryService categoryService;
    private final UserService userService;

    private final AuthorityUtil authorityUtil;

    public List<ComplaintResponseDto> getAllComplaints(int page, int size, String sortBy, boolean ascending, String search, ComplaintStatus filter) {
        // to check the user authority
        authorityUtil.checkUser();

        Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        List<Complaint> complaintList;
        if (filter == null) complaintList = complaintRepo.findAll(pageable).getContent();
        else {
            complaintList = complaintRepo.findAllByComplaintStatus(filter, pageable);
        }

        if (complaintList == null) return null;

        List<ComplaintResponseDto> complaintResponseDtoList = new ArrayList<>();

        complaintList.forEach(complaint -> {

            List<Long> imageList = complaint.getImageList() == null ? null : complaint.getImageList().stream().map(i -> i.getId()).toList();

            complaintResponseDtoList.add(new ComplaintResponseDto(complaint.getId()
                    , complaint.getComplaintStatus(), complaint.getCategory().getIssue()
                    , complaint.getDescription(), complaint.getAgent().getId()
                    , complaint.getCustomer().getId(), imageList));
        });

        log.info("if the search is empty then return the whole list");
        if (search.isBlank()) return complaintResponseDtoList;

        log.info("if the is not empty the filter out the thing and return");
        return complaintResponseDtoList.stream().filter(complaint -> complaint.category().contains(search)).toList();
    }

    public Complaint getById(Long id){
        return complaintRepo.findById(id).orElseThrow(()-> new CustomException(HttpStatus.NOT_FOUND,"the complaint is not found with the given id"));
    }

    public ComplaintResponseDto getComplaintById(Long id) {

        Complaint complaint = getById(id);
        List<Long> imageList = complaint.getImageList() == null ? null : complaint.getImageList().stream().map(i -> i.getId()).toList();
        log.info("returning the complaint by the id : {}",id);
        return new ComplaintResponseDto(complaint.getId(),complaint.getComplaintStatus() ,complaint.getCategory().getIssue(),complaint.getDescription(),complaint.getAgent().getId(),complaint.getCustomer().getId(),imageList);
    }
}
