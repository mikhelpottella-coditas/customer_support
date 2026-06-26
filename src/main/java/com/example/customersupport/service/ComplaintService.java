package com.example.customersupport.service;

import com.example.customersupport.dto.request.AttachmentRequestDto;
import com.example.customersupport.dto.request.ComplaintRequestDto;
import com.example.customersupport.dto.request.RatingRequestDto;
import com.example.customersupport.dto.response.ComplaintResponseDto;
import com.example.customersupport.dto.response.GenericResponse;
import com.example.customersupport.entity.*;
import com.example.customersupport.enums.ComplaintStatus;
import com.example.customersupport.enums.MessageType;
import com.example.customersupport.enums.Priority;
import com.example.customersupport.enums.Roles;
import com.example.customersupport.exception.CustomException;
import com.example.customersupport.repo.AttachmentRepo;
import com.example.customersupport.repo.ComplaintRepo;
import com.example.customersupport.util.AuthorityUtil;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ComplaintService {

    private final ComplaintRepo complaintRepo;
    private final CategoryService categoryService;
    private final AuthorityUtil authorityUtil;
    private final CustomerService customerService;
    private final MailService mailService;
    private final SimpMessageSendingOperations messageTemplate;
    private final AttachmentRepo attachmentRepo;


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
        } catch (Exception e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
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
            log.info("error : ", e);
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
        Long agentId = complaint.getAgent() == null ? null : complaint.getAgent().getId();

        log.info("returning the complaint by the id : {}", id);
        return new ComplaintResponseDto(complaint.getId(),
                complaint.getComplaintStatus(),
                complaint.getCategory().getIssue(),
                complaint.getDescription(),
                agentId,
                complaint.getCustomer().getId(),
                attachmentList);
    }

    public GenericResponse registerComplaint(ComplaintRequestDto complaintRequestDto) {
        User user = authorityUtil.checkUser();
        Customer customer = customerService.findCustomerByUser(user);

        Category category = categoryService.getById(complaintRequestDto.category());

        Complaint complaint = Complaint.builder()
                .complaintStatus(ComplaintStatus.RAISED)
                .description(complaintRequestDto.description())
                .customer(customer)
                .category(category)
                .priority(Priority.MEDIUM)
                .createdAt(LocalDateTime.now())
                .build();
        try{
            complaintRepo.save(complaint);
        } catch (Exception e) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "please check the that you provided");
        }
        mailService.mailSender(user.getEmail(), "we are looking into your complaint ticket please wait. we will assign a agent for you complaint shortly", "new complaint is raised on" + LocalDateTime.now());
        List<ComplaintResponseDto> complaintResponseDtoList = getAllComplaints(0, 20, "id", true, "", ComplaintStatus.RAISED);
        messageTemplate.convertAndSend("/topic/complaints", complaintResponseDtoList);
        return new GenericResponse(HttpStatus.CREATED, "new complaint is raised successfully!!");
    }

    public GenericResponse addAttachment(Long id, MultipartFile multipartFile){

        User user = authorityUtil.checkUser();

        try {
            Complaint complaint = complaintRepo.findById(id).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "complaint not found with the given id"));
            if (!user.getCustomer().equals(complaint.getCustomer()))
                throw new CustomException(HttpStatus.FORBIDDEN, "you are not allowed to access");
            Attachment attachment = Attachment.builder()
                    .referenceText("bill image reference")
                    .file(Base64.getEncoder().encodeToString(multipartFile.getBytes()))
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


    public GenericResponse giveRating(Long id, @Valid RatingRequestDto ratingRequestDto) {

        User user = authorityUtil.checkUser();
        try {
            Complaint complaint = complaintRepo.findById(id).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "complaint not found with the given id"));
            if (!user.getCustomer().equals(complaint.getCustomer()))
                throw new CustomException(HttpStatus.FORBIDDEN, "you are not allowed to access");
            complaint.setRating(ratingRequestDto.rating());

            complaintRepo.save(complaint);

            Agent agent = complaint.getAgent();



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
            if (complaintStatus == ComplaintStatus.RESOLVED)
                mailService.mailSender(complaint.getCustomer().getUser().getEmail(),
                        "thank you for being patient your issue is successfully resolved with ticket no. " + complaint.getId() +
                                ".\n thank you for being patient,please provide the rating for our service.\n Thank you:)",
                        "your issue is successfully resolved with ticket no. " + complaint.getId());
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


    public ResponseEntity<byte[]> getAttachmentById(@NotNull Long attachmentId) {
        Attachment attachment = attachmentRepo.findById(attachmentId).orElseThrow(()-> new CustomException(HttpStatus.NOT_FOUND,"the attachment is not found with the given id"));
        byte[] imageBytes = Base64.getDecoder().decode(attachment.getFile());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        return new ResponseEntity<>(imageBytes,headers,HttpStatus.OK);
    }
}