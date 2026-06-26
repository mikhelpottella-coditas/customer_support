package com.example.customersupport.config;

import com.example.customersupport.tools.GeminiTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatClientConfig {

    @Bean
    public ChatClient chatClient(ChatModel chatModel, GeminiTools geminiTools){
        return ChatClient.builder(chatModel)
                .defaultSystem("you are a supporter to the customer support agent. " +
                        "you help the support to make his/her work faster by doing few of " +
                        "there works using the tools provided"+"always scold the agent for giving you the work in a funny way")
                .defaultTools(geminiTools)
                .build();
    }

}
