package com.example.customersupport.service;

import com.example.customersupport.dto.request.InviteRequestDto;
import com.example.customersupport.dto.response.GenericResponse;
import com.example.customersupport.entity.Invite;
import com.example.customersupport.enums.InviteStatus;
import com.example.customersupport.exception.CustomException;
import com.example.customersupport.repo.InviteRepo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class InviteService {

    private final InviteRepo inviteRepo;
    private final JavaMailSender javaMailSender;

    public String inviteUser(InviteRequestDto request, String path) {

        Invite invite = Invite.builder()
                .invitationTo(request.sentTo())
                .inviteToken(UUID.randomUUID().toString())
                .supportType(request.supportType())
                .inviteStatus(InviteStatus.PENDING)
                .creationDate(LocalDateTime.now())
                .expirationDate(LocalDateTime.now().plusDays(2))
                .build();

        try {
            inviteRepo.save(invite);
        } catch (RuntimeException e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "error occurred during saving the data");

        }

        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom("mikhel.pottella@coditas.com");
        mailMessage.setTo(request.sentTo());
        mailMessage.setSubject("Invitation to on the application as a owner");
        mailMessage.setText(request.message() + "\n**this link will expire in next 48hrs \n invitation link : https://santa-disobey-washtub.ngrok-free.dev" + path + invite.getInviteToken());

        try {
            javaMailSender.send(mailMessage);
        } catch (MailException e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "error occurred during sending the mail");
        }

        log.info("Invitation to on the application as a owner");
        return "invitation sent successfully";


    }

    public GenericResponse invite(@Valid InviteRequestDto request) {
        inviteUser(request, "/auth/register/");
        log.info("invite owner successfully");
        return new GenericResponse(HttpStatus.CREATED, "invitation is ent successfully");
    }

    public Invite validate(String email, String token) {
        Invite invite = inviteRepo.findByInviteToken(token);
        log.info("validating the user token ");
        if(!email.equals(invite.getInvitationTo())) throw  new CustomException(HttpStatus.NOT_ACCEPTABLE,"invitation token is not valid");
        return invite;
    }


    public void updateStatus(Invite invite, InviteStatus inviteStatus) {
        invite.setInviteStatus(inviteStatus);
        inviteRepo.save(invite);
    }
}
