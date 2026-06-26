package com.example.customersupport.service;

import com.example.customersupport.dto.response.GenericResponse;
import com.example.customersupport.entity.Chatting;
import com.example.customersupport.entity.Complaint;
import com.example.customersupport.entity.User;
import com.example.customersupport.enums.Roles;
import com.example.customersupport.exception.CustomException;
import com.example.customersupport.repo.ChattingRepo;
import com.example.customersupport.util.AuthorityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {

    private final ChattingRepo chattingRepo;
    private final AuthorityUtil authorityUtil;
    private final ComplaintService complaintService;
    private final SimpMessageSendingOperations messageTemplate;


    public GenericResponse msgSender(Long complaintId, String msg) {

        User user = authorityUtil.checkUser();

        Complaint complaint = complaintService.getById(complaintId);

        // checking the authority of the user
        if (user.getRole().equals(Roles.AGENT) && user.getAgent() != complaint.getAgent()) {
            throw new CustomException(HttpStatus.FORBIDDEN, "you dont have authority");
        }
        if (user.getRole().equals(Roles.CUSTOMER) && user.getCustomer() != complaint.getCustomer()) {
            throw new CustomException(HttpStatus.FORBIDDEN, "you dont have authority");
        }

        Chatting chatting =Chatting.builder()
                .senderId(user.getId())
                .SenderName(user.getFirstName())
                .message(msg)
                .createdAt(LocalDateTime.now())
                .complaint(complaint)
                .build();

        chattingRepo.save(chatting);

        messageTemplate.convertAndSend("topic/chat/",chatting.toString());

        return new GenericResponse(HttpStatus.OK,"sent");

    }
}
