package com.example.customersupport.service;


import com.example.customersupport.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailService {

    private final JavaMailSender javaMailSender;


    public void mailSender(String sentTo,String msg,String sub) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom("mikhel.pottella@coditas.com");
        mailMessage.setTo(sentTo);
        mailMessage.setSubject(sub);
        mailMessage.setText(msg);

        try{
            javaMailSender.send(mailMessage);
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR,"something went wrong");
        }


    }


}
