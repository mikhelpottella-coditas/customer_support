package com.example.customersupport.service;

import com.example.customersupport.dto.request.AttachmentRequestDto;
import com.example.customersupport.dto.request.ComplaintRequestDto;
import com.example.customersupport.dto.request.RatingRequestDto;
import com.example.customersupport.dto.response.ComplaintResponseDto;
import com.example.customersupport.dto.response.GenericResponse;
import com.example.customersupport.entity.*;
import com.example.customersupport.enums.ComplaintStatus;
import com.example.customersupport.enums.Priority;
import com.example.customersupport.enums.Roles;
import com.example.customersupport.exception.CustomException;
import com.example.customersupport.repo.ComplaintRepo;
import com.example.customersupport.util.AuthorityUtil;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

        if (user.getRole() == Roles.MANAGER) {
            if (filter == null) complaintList = complaintRepo.findAll(pageable).getContent();
            else {
                complaintList = complaintRepo.findAllByComplaintStatus(filter, pageable);
            }
        } else if (user.getRole() == Roles.AGENT) {
            if (filter == null) complaintList = complaintRepo.findAllByAgent_User(user, pageable).getContent();
            else {
                complaintList = complaintRepo.findAllByComplaintStatusAndAgent_User(filter, user, pageable).getContent();
            }
        } else {
            if (filter == null) complaintList = complaintRepo.findAllByCustomer_User(user, pageable).getContent();
            else {
                complaintList = complaintRepo.findAllByComplaintStatusAndCustomer_User(filter, user, pageable).getContent();
            }
        }


        if (complaintList == null) return null;

        List<ComplaintResponseDto> complaintResponseDtoList = new ArrayList<>();
        try {


            complaintList.forEach(complaint -> {

                List<Long> attachemtList = complaint.getAttachmentList() == null ? null : complaint.getAttachmentList().stream().map(i -> i.getId()).toList();

                complaintResponseDtoList.add(ComplaintResponseDto.builder()
                        .id(complaint.getId())
                        .complaintStatus(complaint.getComplaintStatus())
                        .category(complaint.getCategory().getIssue())
                        .description(complaint.getDescription())
                        .CustomerId(complaint.getCustomer().getId())
                        .agentId(complaint.getAgent().getId())
                        .attachmentList(attachemtList)
                        .build());
            });
        } catch (Exception e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "something went wrong");
        }
        log.info("if the search is empty then return the whole list");
        if (search.isBlank()) return complaintResponseDtoList;

        log.info("if the is not empty the filter out the thing and return");
        return complaintResponseDtoList.stream().filter(complaint -> complaint.category().contains(search)).toList();
    }

    public Complaint getById(Long id) {
        return complaintRepo.findById(id).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "the complaint is not found with the given id"));
    }


    public ComplaintResponseDto getComplaintById(Long id) {
        User user = authorityUtil.checkUser();

        Complaint complaint;
        try {
            complaint = getById(id);
        } catch (Exception e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "something went wrong");
        }

        // checking the authority of the user
        if (user.getRole().equals(Roles.AGENT) && user.getAgent() != complaint.getAgent()) {
            throw new CustomException(HttpStatus.FORBIDDEN, "you dont have authority");
        }
        if (user.getRole().equals(Roles.CUSTOMER) && user.getCustomer() != complaint.getCustomer()) {
            throw new CustomException(HttpStatus.FORBIDDEN, "you dont have authority");
        }


        List<Long> attachmentList = complaint.getAttachmentList() == null ? null : complaint.getAttachmentList().stream().map(i -> i.getId()).toList();
        log.info("returning the complaint by the id : {}", id);
        return new ComplaintResponseDto(complaint.getId(), complaint.getComplaintStatus(), complaint.getCategory().getIssue(), complaint.getDescription(), complaint.getAgent().getId(), complaint.getCustomer().getId(), attachmentList);
    }

    public GenericResponse registerComplaint(ComplaintRequestDto complaintRequestDto) {
        User user = authorityUtil.checkUser();
        Customer customer = customerService.findCustomerByUser(user);

        Complaint complaint = Complaint.builder()
                .complaintStatus(ComplaintStatus.RAISED)
                .description(complaintRequestDto.description())
                .customer(customer)
                .category(complaintRequestDto.category())
                .priority(Priority.MEDIUM)
                .createdAt(LocalDateTime.now())
                .build();

        try {
            complaintRequestDto.attachmentRequestDtoList().forEach(attachmentRequestDto -> {
                Attachment attachment = Attachment.builder()
                        .file(attachmentRequestDto.file())
                        .referenceText(attachmentRequestDto.referenceText())
                        .build();
                complaint.addAttachment(attachment);
            });
            complaintRepo.save(complaint);
        } catch (Exception e) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "please check the that you provided");
        }
        return new GenericResponse(HttpStatus.CREATED, "new complaint is raised successfully!!");
    }

    public GenericResponse addAttachment(Long id, @Valid AttachmentRequestDto attachmentRequestDto) {

        User user = authorityUtil.checkUser();

        try {
            Complaint complaint = complaintRepo.findById(id).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "complaint not found with the given id"));
            if (!user.getCustomer().equals(complaint.getCustomer()))
                throw new CustomException(HttpStatus.FORBIDDEN, "you are not allowed to access");
            Attachment attachment = Attachment.builder()
                    .referenceText(attachmentRequestDto.referenceText())
                    .file(attachmentRequestDto.file())
                    .build();
            //adding the attachment to the complaint
            complaint.addAttachment(attachment);
            complaintRepo.save(complaint);
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "server is slow please wait");
        }
        log.info("the attachment is added to the complaint ");
        return new GenericResponse(HttpStatus.CREATED, "attachment is added to the complaint!!");

    }

    public GenericResponse sendMessage(Long id, String message) {

        User user = authorityUtil.checkUser();
        try {
            Complaint complaint = complaintRepo.findById(id).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "complaint not found with the given id"));

            // checking the authority of the user
            if (user.getRole().equals(Roles.AGENT) && user.getAgent() != complaint.getAgent()) {
                throw new CustomException(HttpStatus.FORBIDDEN, "you dont have authority");
            }
            if (user.getRole().equals(Roles.CUSTOMER) && user.getCustomer() != complaint.getCustomer()) {
                throw new CustomException(HttpStatus.FORBIDDEN, "you dont have authority");
            }

            Chatting chatting = Chatting.builder()
                    .senderId(user.getId())
                    .message(message)
                    .SenderName(user.getFirstName())
                    .build();

            complaint.addChatting(chatting);
            complaintRepo.save(complaint);
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "something went wrong");
        }
        log.info("message is sent by the user id : {}", user.getId());
        return new GenericResponse(HttpStatus.CREATED, "message sent successfully");
    }


    public GenericResponse giveRating(Long id, @Valid RatingRequestDto ratingRequestDto) {

        User user = authorityUtil.checkUser();
        try {
            Complaint complaint = complaintRepo.findById(id).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "complaint not found with the given id"));
            if (!user.getCustomer().equals(complaint.getCustomer()))
                throw new CustomException(HttpStatus.FORBIDDEN, "you are not allowed to access");
            complaint.setRating(ratingRequestDto.rating());

            complaintRepo.save(complaint);
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "something went wrong");
        }
        return new GenericResponse(HttpStatus.CREATED, "thank you for your rating :)");
    }

    public GenericResponse updateStatus(Long id, ComplaintStatus complaintStatus) {
        User user = authorityUtil.checkUser();
        try {
            Complaint complaint = complaintRepo.findById(id).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "complaint not found with the given id"));
            if (!user.getCustomer().equals(complaint.getCustomer()))
                throw new CustomException(HttpStatus.FORBIDDEN, "you are not allowed to access");
            complaint.setComplaintStatus(complaintStatus);
            complaintRepo.save(complaint);
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "something went wrong");
        }

        return new GenericResponse(HttpStatus.OK, "status update is successful");
    }

    public void saveCompliant(Complaint complaint) {
        complaintRepo.save(complaint);
    }

    public GenericResponse setPriority(Long id, Priority priority) {
        Complaint complaint = getById(id);

        complaint.setPriority(priority);

        try {
            complaintRepo.save(complaint);
        } catch (Exception e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "something went wrong!");
        }

        log.info("priority is updated successfully");
        return new GenericResponse(HttpStatus.OK, "priority is updated successfully");
    }
}