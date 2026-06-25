package com.example.customersupport.service;

import com.example.customersupport.dto.request.ComplaintRequestDto;
import com.example.customersupport.dto.response.ComplaintResponseDto;
import com.example.customersupport.dto.response.GenericResponse;
import com.example.customersupport.entity.Attachment;
import com.example.customersupport.entity.Complaint;
import com.example.customersupport.entity.Customer;
import com.example.customersupport.entity.User;
import com.example.customersupport.enums.ComplaintStatus;
import com.example.customersupport.enums.Roles;
import com.example.customersupport.exception.CustomException;
import com.example.customersupport.repo.ComplaintRepo;
import com.example.customersupport.util.AuthorityUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.constraintvalidators.bv.number.bound.decimal.AbstractDecimalMinValidator;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    private final CustomerService customerService;


    public List<ComplaintResponseDto> getAllComplaints(int page, int size, String sortBy, boolean ascending, String search, ComplaintStatus filter) {
        // to check the user authority

        User user = authorityUtil.checkUser();

        Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        List<Complaint> complaintList;

        if(user.getRole()== Roles.MANAGER) {
            if (filter == null) complaintList = complaintRepo.findAll(pageable).getContent();
            else {
                complaintList = complaintRepo.findAllByComplaintStatus(filter, pageable);
            }
        } else if (user.getRole()==Roles.AGENT) {
            if (filter == null) complaintList = complaintRepo.findAllByAgent_User(user,pageable).getContent();
            else {
                complaintList = complaintRepo.findAllByComplaintStatusAndAgent_User(filter,user, pageable).getContent();
            }
        }else {
            if (filter == null) complaintList = complaintRepo.findAllByCustomer_User(user,pageable).getContent();
            else {
                complaintList = complaintRepo.findAllByComplaintStatusAndCustomer_User(filter,user, pageable).getContent();
            }
        }


        if (complaintList == null) return null;

        List<ComplaintResponseDto> complaintResponseDtoList = new ArrayList<>();

        complaintList.forEach(complaint -> {

            List<Long> attachemtList = complaint.getAttachmentList() == null ? null : complaint.getAttachmentList().stream().map(i -> i.getId()).toList();

            complaintResponseDtoList.add(new ComplaintResponseDto(complaint.getId()
                    , complaint.getComplaintStatus(), complaint.getCategory().getIssue()
                    , complaint.getDescription(), complaint.getAgent().getId()
                    , complaint.getCustomer().getId(), attachemtList));
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
        List<Long> attachmentList = complaint.getAttachmentList() == null ? null : complaint.getAttachmentList().stream().map(i -> i.getId()).toList();
        log.info("returning the complaint by the id : {}",id);
        return new ComplaintResponseDto(complaint.getId(),complaint.getComplaintStatus() ,complaint.getCategory().getIssue(),complaint.getDescription(),complaint.getAgent().getId(),complaint.getCustomer().getId(),attachmentList);
    }

    public GenericResponse registerComplaint(ComplaintRequestDto complaintRequestDto) {
        User user = authorityUtil.checkUser();
        Customer customer = customerService.findCustomerByUser(user);

        List<Attachment> attachmentList = new ArrayList<>();
        Complaint complaint = Complaint.builder()
                .complaintStatus(ComplaintStatus.RAISED)
                .description(complaintRequestDto.description())
                .customer(customer)
                .category(complaintRequestDto.category())
                .createdAt(LocalDateTime.now())
                .build();

        try{
            complaintRequestDto.attachmentRequestDtoList().forEach(attachmentRequestDto ->{
                Attachment attachment = Attachment.builder()
                        .file(attachmentRequestDto.file())
                        .referenceText(attachmentRequestDto.referenceText())
                        .build();
                complaint.addAttachment(attachment);
            });
            complaintRepo.save(complaint);
        } catch (Exception e) {
            throw new CustomException(HttpStatus.BAD_REQUEST,"please check the that you provided");
        }
        return new GenericResponse(HttpStatus.CREATED,"new complaint is raised successfully!!");
    }
}
