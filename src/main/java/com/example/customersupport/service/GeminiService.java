package com.example.customersupport.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.Generation;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GeminiService {
    private final ChatClient chatClient;

    public String  getAnswer(String question) {
        String response = chatClient
                .prompt()
                .user(question)
                .call()
                        .content();


        log.info("Response from Gemini: {}", response);
        return response;
    }
}